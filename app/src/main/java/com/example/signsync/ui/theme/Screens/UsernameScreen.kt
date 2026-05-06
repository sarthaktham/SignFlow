

package com.example.signsync.ui.theme.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.signsync.AuthViewModel
import com.example.signsync.DarkBlue
import com.example.signsync.MediumPoppins
import com.example.signsync.R
import com.example.signsync.Routes
import com.example.signsync.TransparentCardColor

@Composable
fun UsernameScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.app_background_image__2_),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = TransparentCardColor)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "What's your name?",
                        fontSize = 24.sp,
                        fontFamily = MediumPoppins,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Enter your name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            // Update the username in ViewModel
                            authViewModel.updateDisplayName(name)
                            navController.navigate(Routes.WELCOME) {
                                popUpTo(Routes.USERNAME) { inclusive = true }
                            }
                        },
                        enabled = name.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Continue", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}