package com.example.signsync.ui.theme.Screens

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetectionScreen(navController: NavController) {

    val viewModel: DetectionViewModel = viewModel()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val sign by viewModel.sign.collectAsStateWithLifecycle()
    val confidence by viewModel.confidence.collectAsStateWithLifecycle()
    val sentence by viewModel.sentence.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val hapticTrigger by viewModel.hapticTrigger.collectAsStateWithLifecycle()

    var hapticEnabled by remember { mutableStateOf(true) }
    var voiceEnabled by remember { mutableStateOf(true) }

    // ── Text To Speech Setup ────────────────────────────────
    val tts = remember {
        var speech: TextToSpeech? = null
        speech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                speech?.language = Locale.US
            }
        }
        speech
    }

    // Speak when sign changes
    LaunchedEffect(sign) {
        if (voiceEnabled && !sign.isNullOrEmpty()) {
            val toSpeak = if (sign == "Space") "Space" else sign!!
            tts?.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    // ── Vibrator Setup ───────────────────────────────────────
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    LaunchedEffect(hapticTrigger) {
        if (hapticTrigger == 0 || !hapticEnabled) return@LaunchedEffect

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = when (sign) {
                "Space" -> VibrationEffect.createWaveform(longArrayOf(0, 70, 50, 70), -1) // Double pulse
                else -> VibrationEffect.createOneShot(100L, VibrationEffect.DEFAULT_AMPLITUDE) // Single pulse
            }
            vibrator.vibrate(effect)
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100L)
        }
    }

    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val previewView = remember { PreviewView(context) }
    var lastSentTime by remember { mutableStateOf(0L) }

    LaunchedEffect(cameraPermission.status.isGranted) {
        if (!cameraPermission.status.isGranted) return@LaunchedEffect
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
        val analyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().also { analysis ->
                analysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    val now = System.currentTimeMillis()
                    if (now - lastSentTime >= 1000L) {
                        lastSentTime = now
                        viewModel.predict(imageProxy.toBitmap())
                    }
                    imageProxy.close()
                }
            }
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_FRONT_CAMERA, preview, analyzer)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.app_background_image__2_),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (!cameraPermission.status.isGranted) {
            PermissionDeniedUI(
                shouldShowRationale = cameraPermission.status.shouldShowRationale,
                onRequest = { cameraPermission.launchPermissionRequest() },
                onBack = { navController.popBackStack() }
            )
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                CenterAlignedTopAppBar(
                    title = { Text("Sign Detection", fontWeight = FontWeight.Bold, color = WhiteText) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = WhiteText)
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            voiceEnabled = !voiceEnabled
                            if(voiceEnabled) tts?.speak("Voice On", TextToSpeech.QUEUE_FLUSH, null, null)
                        }) {
                            Text(if (voiceEnabled) "🔊" else "🔇", fontSize = 22.sp)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBlue.copy(alpha = 0.85f))
                )

                Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

                    if (isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = WhiteText)

                    // Detected Sign Card (Visual)
                    if (sign != null) {
                        Card(
                            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkBlue.copy(alpha = 0.85f)),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(sign ?: "", fontSize = 68.sp, fontWeight = FontWeight.ExtraBold, color = WhiteText)
                                Text("${(confidence * 100).toInt()}%", fontSize = 12.sp, color = WhiteText.copy(0.7f))
                            }
                        }
                    }
                }

                // Sentence & Control Panel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkBlue.copy(alpha = 0.95f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Haptic Feedback", color = WhiteText, modifier = Modifier.weight(1f))
                            Switch(
                                checked = hapticEnabled,
                                onCheckedChange = { hapticEnabled = it },
                                colors = SwitchDefaults.colors(checkedTrackColor = Color.Green)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Detected Sentence", fontSize = 12.sp, color = WhiteText.copy(0.5f))
                        Text(
                            sentence.ifEmpty { "..." },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = WhiteText,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 40.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            OutlinedButton(onClick = {
                                viewModel.clearSentence()
                                tts?.speak("Cleared", TextToSpeech.QUEUE_FLUSH, null, null)
                            }) {
                                Icon(Icons.Default.Delete, "Clear", modifier = Modifier.size(18.dp))
                                Text(" Clear", color = WhiteText)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionDeniedUI(shouldShowRationale: Boolean, onRequest: () -> Unit, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(DarkBlue.copy(0.95f)), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Text("Camera Access Required", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = WhiteText)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onRequest, colors = ButtonDefaults.buttonColors(containerColor = GlassyButtonColor)) {
                Text("Enable Camera", color = DarkBlue)
            }
            TextButton(onClick = onBack) { Text("Go Back", color = WhiteText) }
        }
    }
}