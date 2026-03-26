package com.example.signsync.ui.theme.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
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
import com.example.signsync.DarkBlue
import com.example.signsync.GlassyButtonColor
import com.example.signsync.MediumPoppins
import com.example.signsync.R
import com.example.signsync.WhiteText

// ----------------------------------------------------------------------
// ## ℹ️ AboutUsScreen (with Navigation)
// ----------------------------------------------------------------------


@Composable
fun AboutUsScreen(navController: NavController) {
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
                            "About Us",
                            fontSize = 28.sp,
                            fontFamily = MediumPoppins,
                            fontWeight = FontWeight.ExtraBold,
                            color = WhiteText
                        )
                    },
                    navigationIcon = {
                        // Back button functionality
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Using Menu as a placeholder for back icon
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
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // --- Motivation Section ---
                    AestheticCard(
                        title = "Our Motivation and Objective",
                        content = "The SignFlow app aims to break communication barriers by providing seamless real-time translation from Indian Sign Language (ISL) to text and voice. Our objective is to empower the deaf community and foster greater inclusivity through technology and education."
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // --- Team Section Header ---
                    Text(
                        text = "Meet the Team",
                        fontFamily = MediumPoppins,
                        fontWeight = FontWeight.ExtraBold,
                        color = WhiteText,
                        fontSize = 26.sp,
                    )

                    // --- Team Members List (Centered Cards)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(25.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TeamMember(name = "Nakul Pise", role = "Full-Stack Developer")
                        TeamMember(name = "Pranav Kshirsagar", role = "Full-Stack Developer")
                        TeamMember(name = "Tanishq Tiwari", role = "ML Developer")
                        TeamMember(name = "Sarthak Thamke", role = "Android Developer")
                    }
                }
            }
        )
    }
}

// Custom Card for text sections in AboutUsScreen
@Composable
fun AestheticCard(title: String, content: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = GlassyButtonColor.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontFamily = MediumPoppins,
                fontWeight = FontWeight.ExtraBold,
                color = DarkBlue,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Divider(color = DarkBlue.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                fontFamily = MediumPoppins,
                fontWeight = FontWeight.Medium,
                color = DarkBlue.copy(alpha = 0.9f),
                fontSize = 16.sp,
                textAlign = TextAlign.Justify
            )
        }
    }
}

// Composable for Team Member Profile Card
@Composable
fun TeamMember(name: String, role: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(0.75f),
        colors = CardDefaults.cardColors(containerColor = WhiteText.copy(alpha = 0.8f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Profile Icon for $name",
                modifier = Modifier.size(96.dp),
                tint = DarkBlue.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontFamily = MediumPoppins,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = DarkBlue
            )
            Text(
                text = role,
                fontFamily = MediumPoppins,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = DarkBlue.copy(alpha = 0.7f)
            )
        }
    }
}