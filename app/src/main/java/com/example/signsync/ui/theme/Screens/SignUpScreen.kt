package com.example.signsync.ui.theme.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.signsync.AuthState
import com.example.signsync.AuthViewModel
import com.example.signsync.DarkBlueAccent
import com.example.signsync.MediumPoppins
import com.example.signsync.R
import com.example.signsync.Routes
import com.example.signsync.TransparentCardColor

@Composable
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate(Routes.WELCOME) {
                    popUpTo(Routes.SIGNUP) { inclusive = true }
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.app_background_image__2_),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.gemini_generated_image_nddaktnddaktndda),
                contentDescription = "applogo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = TransparentCardColor)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Create Account",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 28.sp,
                        fontFamily = MediumPoppins,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Color.Black.copy(alpha = 0.8f)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (authState is AuthState.Error) {
                        Text(
                            text = (authState as AuthState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = { authViewModel.signUp(email, password, confirmPassword) },
                        enabled = authState !is AuthState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (authState is AuthState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Create Account", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account?", color = Color.Black, fontFamily = MediumPoppins)
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Sign In", fontWeight = FontWeight.Bold, color = DarkBlueAccent)
                }
            }
        }
    }
}

