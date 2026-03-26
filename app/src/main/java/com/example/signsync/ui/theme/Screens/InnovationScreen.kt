package com.example.signsync.ui.theme.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.signsync.R
import com.example.signsync.DarkBlue
import com.example.signsync.MediumPoppins
import com.example.signsync.TransparentCardColor
import com.example.signsync.WhiteText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InnovationScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.app_background_image__2_),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Innovation: ISL Translation",
                            fontSize = 26.sp,
                            fontFamily = MediumPoppins,
                            fontWeight = FontWeight.ExtraBold,
                            color = WhiteText
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = WhiteText
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = DarkBlue.copy(alpha = 0.8f),
                        titleContentColor = WhiteText
                    )
                )
            },
            containerColor = Color.Transparent,
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InnovationCard()
                }
            }
        )
    }
}

@Composable
fun InnovationCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TransparentCardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "How SignFlow Translates ISL to Text/Voice",
                fontFamily = MediumPoppins,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                lineHeight = 36.sp,
                color = DarkBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Divider(color = DarkBlue.copy(alpha = 0.3f), thickness = 2.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "The SignFlow system uses advanced Computer Vision and Sequence Modeling to achieve real-time Indian Sign Language (ISL) recognition. This process involves several distinct, sequential stages:",
                fontFamily = MediumPoppins,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = DarkBlue.copy(alpha = 0.9f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // --- Step-by-Step Breakdown ---

            // Step 1: Data Acquisition
            InnovationSubPoint(
                number = 1,
                title = "Video Stream Acquisition",
                description = "The process begins with capturing a live video stream of the user signing. The system continuously analyzes frames to identify the start and end of a sign phrase."
            )

            // Step 2: Pose and Hand Landmark Detection
            InnovationSubPoint(
                number = 2,
                title = "Pose and Hand Landmark Detection",
                description = "Specialized machine learning models (like MediaPipe Holistic) are used to detect 2D coordinates (landmarks) for key points on the user's hands, face, and body. These points define the **shape, orientation, and movement** of the sign."
            )


            // Step 3: Feature Extraction & Normalization
            InnovationSubPoint(
                number = 3,
                title = "Feature Extraction & Normalization",
                description = "The raw landmark coordinates are converted into robust, relative features (e.g., angles between finger joints, distances from the hand to the nose). This ensures the system recognizes the sign regardless of the user's size or distance."
            )

            // Step 4: Sequence Recognition (LSTM/Transformer)
            InnovationSubPoint(
                number = 4,
                title = "Sequence Recognition (LSTM/Transformer)",
                description = "The normalized features are fed as a time sequence into a recurrent neural network (such as an LSTM) or a Transformer model. This model classifies the dynamic motion and hand shapes into a recognized ISL word or sentence structure."
            )

            // Step 5: Output Generation (Text & Voice)
            InnovationSubPoint(
                number = 5,
                title = "Output Generation (Text & Voice)",
                description = "The recognized sign is mapped to the corresponding linguistic output. This output is displayed as **Text** and simultaneously converted into audible speech using a **Text-to-Speech (TTS)** engine for immediate verbal communication."
            )
        }
    }
}

@Composable
fun InnovationSubPoint(number: Int, title: String, description: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        // Number Marker
        Text(
            text = "$number.",
            fontFamily = MediumPoppins,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = DarkBlue,
            modifier = Modifier.padding(end = 8.dp).align(Alignment.Top)
        )
        Column {
            Text(
                text = title,
                fontFamily = MediumPoppins,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkBlue,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontFamily = MediumPoppins,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                color = DarkBlue.copy(alpha = 0.8f),
                textAlign = TextAlign.Justify
            )
        }
    }
}

