package com.example.signsync.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.signsync.DarkBlue
import com.example.signsync.GlassyButtonColor
import com.example.signsync.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionScreen(navController: NavController) {
    val primaryColor = try { DarkBlue } catch (e: Exception) { Color(0xFF001F3F) }
    val cardBackground = try { GlassyButtonColor.copy(alpha = 0.3f) } catch (e: Exception) { Color.LightGray.copy(alpha = 0.3f) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.how_to_use), fontWeight = FontWeight.Bold, color = primaryColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = primaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Standard Instructions
                InstructionStep("1", Icons.Default.CameraAlt, "Position your Camera", "Place your phone stably. Ensure your upper body and hands are visible.", primaryColor, cardBackground)

                // Voice Feature
                InstructionStep("2", Icons.Default.VolumeUp, "Audio Feedback (TTS)", "The app speaks out detected signs in real-time. Use the speaker icon to toggle voice on or off.", primaryColor, cardBackground)

                // Haptic Feature (Crucial for Accessibility)
                InstructionStep("3", Icons.Default.Vibration, "Haptic Vibration", "Feel the detection! A single pulse means a letter is detected. A double pulse indicates a 'Space'.", primaryColor, cardBackground)

                // Environment
                InstructionStep("4", Icons.Default.LightMode, "Lighting Matters", "Make sure the room is well-lit. Avoid having bright windows directly behind you.", primaryColor, cardBackground)
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Got it!", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
        }
    }
}

@Composable
fun InstructionStep(
    number: String,
    icon: ImageVector,
    title: String,
    description: String,
    themeColor: Color,
    bgColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = themeColor,
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = themeColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text(description, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
            }
        }
    }
}