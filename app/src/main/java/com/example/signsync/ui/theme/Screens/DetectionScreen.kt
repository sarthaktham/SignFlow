package com.example.signsync.ui.theme.Screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.signsync.DarkBlue
import com.example.signsync.GlassyButtonColor
import com.example.signsync.R
import com.example.signsync.WhiteText
import com.example.signsync.viewmodel.DetectionViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetectionScreen(navController: NavController) {

    val viewModel: DetectionViewModel = viewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ── Observe ViewModel state ──────────────────────────────
    val sign          by viewModel.sign.collectAsStateWithLifecycle()
    val confidence    by viewModel.confidence.collectAsStateWithLifecycle()
    val sentence      by viewModel.sentence.collectAsStateWithLifecycle()
    val isLoading     by viewModel.isLoading.collectAsStateWithLifecycle()
    val error         by viewModel.error.collectAsStateWithLifecycle()
    val hapticTrigger by viewModel.hapticTrigger.collectAsStateWithLifecycle()

    // ── Haptic toggle — ON by default ────────────────────────
    var hapticEnabled by remember { mutableStateOf(true) }

    // ── Vibrator setup ───────────────────────────────────────
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    // ── Vibrate every time hapticTrigger increments ──────────
    LaunchedEffect(hapticTrigger) {
        if (hapticTrigger == 0) return@LaunchedEffect
        if (!hapticEnabled) return@LaunchedEffect

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            when {
                sign?.length == 1 -> vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        80L,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
                sign == "Space" -> vibrator.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 60, 60, 60), -1
                    )
                )
                else -> vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200L,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100L)
        }
    }

    // ── Camera permission ────────────────────────────────────
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(Unit) { cameraPermission.launchPermissionRequest() }

    // ── Camera setup ─────────────────────────────────────────
    val previewView = remember { PreviewView(context) }
    var lastSentTime by remember { mutableStateOf(0L) }

    LaunchedEffect(cameraPermission.status.isGranted) {
        if (!cameraPermission.status.isGranted) return@LaunchedEffect

        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val analyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also { analysis ->
                analysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context)
                ) { imageProxy ->
                    val now = System.currentTimeMillis()
                    if (now - lastSentTime >= 1000L) {
                        lastSentTime = now
                        val bitmap = imageProxy.toBitmap()
                        viewModel.predict(bitmap)
                    }
                    imageProxy.close()
                }
            }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_FRONT_CAMERA,
            preview,
            analyzer
        )
    }

    // ── Root Box ─────────────────────────────────────────────
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.app_background_image__2_),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ── Permission denied ────────────────────────────────
        if (!cameraPermission.status.isGranted) {
            PermissionDeniedUI(
                shouldShowRationale = cameraPermission.status.shouldShowRationale,
                onRequest = { cameraPermission.launchPermissionRequest() },
                onBack = { navController.popBackStack() }
            )
        } else {

            Column(modifier = Modifier.fillMaxSize()) {

                // ── Top bar ──────────────────────────────────
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Sign Detection",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = WhiteText
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = WhiteText
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = DarkBlue.copy(alpha = 0.85f)
                    )
                )

                // ── Camera preview area ───────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // Live camera feed
                    AndroidView(
                        factory = { previewView },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Loading spinner
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = WhiteText,
                            modifier = Modifier
                                .size(52.dp)
                                .align(Alignment.Center)
                        )
                    }

                    // Error card — top center
                    if (error != null) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(16.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Red.copy(alpha = 0.88f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = error ?: "",
                                    color = WhiteText,
                                    fontSize = 13.sp,
                                    modifier = Modifier.weight(1f),
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(onClick = { viewModel.clearError() }) {
                                    Text(
                                        text = "OK",
                                        color = WhiteText,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }

                    // ── Haptic toggle — top right ─────────────
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = DarkBlue.copy(alpha = 0.82f)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (hapticEnabled) "📳" else "🔕",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Haptic",
                                color = WhiteText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = hapticEnabled,
                                onCheckedChange = { hapticEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = WhiteText,
                                    checkedTrackColor = Color(0xFF4CAF50),
                                    uncheckedThumbColor = WhiteText,
                                    uncheckedTrackColor = Color.Gray
                                )
                            )
                        }
                    }

                    // Detected sign card — bottom left
                    if (sign != null) {
                        Card(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = DarkBlue.copy(alpha = 0.85f)
                            ),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(
                                    horizontal = 24.dp,
                                    vertical = 14.dp
                                ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = sign ?: "",
                                    fontSize = 72.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = WhiteText
                                )
                                Text(
                                    text = "${(confidence * 100).toInt()}% confidence",
                                    fontSize = 13.sp,
                                    color = WhiteText.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                // ── Sentence panel ────────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = DarkBlue.copy(alpha = 0.95f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Sentence",
                            fontSize = 12.sp,
                            color = WhiteText.copy(alpha = 0.55f),
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = sentence.ifEmpty { "Detected signs will appear here..." },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (sentence.isEmpty())
                                WhiteText.copy(alpha = 0.35f)
                            else WhiteText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp),
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.clearSentence() },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = WhiteText
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Clear sentence",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Clear", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Permission Denied UI ─────────────────────────────────────
@Composable
fun PermissionDeniedUI(
    shouldShowRationale: Boolean,
    onRequest: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(
                        color = GlassyButtonColor.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📷", fontSize = 40.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (shouldShowRationale)
                    "Camera Permission Needed"
                else
                    "Camera Permission Denied",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = WhiteText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (shouldShowRationale)
                    "SignSync needs your camera to detect hand signs in real time."
                else
                    "Please go to Settings → Apps → SignSync → Permissions and enable Camera.",
                fontSize = 14.sp,
                color = WhiteText.copy(alpha = 0.72f),
                textAlign = TextAlign.Center,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            if (shouldShowRationale) {
                Button(
                    onClick = onRequest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GlassyButtonColor
                    )
                ) {
                    Text(
                        text = "Allow Camera",
                        color = DarkBlue,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = WhiteText
                )
            ) {
                Text(text = "Go Back", fontSize = 16.sp)
            }
        }
    }
}