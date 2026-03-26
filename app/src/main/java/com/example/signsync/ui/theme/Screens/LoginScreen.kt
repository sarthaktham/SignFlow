package com.example.signsync.ui.theme.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.signsync.DarkBlueAccent
import com.example.signsync.MediumPoppins
import com.example.signsync.R
import com.example.signsync.Routes
import com.example.signsync.TransparentCardColor


@Composable
fun LoginScreen(navController: NavController) {
    // State to hold the user inputs
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginStatus by remember { mutableStateOf<String?>(null) } // Status message

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.app_background_image__2_),
            contentDescription = "image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.gemini_generated_image_nddaktnddaktndda),
                contentDescription = "applogo",
                modifier = Modifier.size(140.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 1. Logo and Title Section (Preserving user's Row structure)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.width(16.dp))
            }

            // 2. Transparent Card for Sign-In Form
            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = TransparentCardColor
                ),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 32.sp,
                        fontFamily = MediumPoppins,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color.Black.copy(alpha = 0.8f)
                    )

                    // Username Field
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username or Email") },
                        leadingIcon = {
                            Icon(Icons.Filled.Person, contentDescription = "Username Icon")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, contentDescription = "Password Icon")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- Sign In Button and Logic ---
                    Button(
                        onClick = {
                            // Conditional Authentication Logic
                            if (username == "user" && password == "password") {
                                navController.navigate(Routes.WELCOME) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                                loginStatus = null
                            } else {
                                loginStatus = "Invalid username or password"
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    // Status Message
                    loginStatus?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // Forgot Password Link
                    TextButton(onClick = { navController.navigate(Routes.WELCOME) }) {
                        Text(
                            text = "Forgot Password?",
                            color = DarkBlueAccent
                        )
                    }
                }
            }

            // Spacer to separate the card from the Create Account option
            Spacer(modifier = Modifier.height(32.dp))

            // 3. Create Account Option
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    color = Color.Black,
                    fontFamily = MediumPoppins
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = { /* TODO: Navigate to Create Account screen */ }) {
                    Text(
                        text = "Create Account",
                        fontWeight = FontWeight.Bold,
                        color = DarkBlueAccent
                    )
                }
            }
        }
    }
}