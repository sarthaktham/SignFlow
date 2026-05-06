package com.example.signsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.signsync.ui.theme.Screens.AboutUsScreen
import com.example.signsync.ui.theme.Screens.DetectionScreen
import com.example.signsync.ui.theme.Screens.FeedbackScreen
import com.example.signsync.ui.theme.Screens.InnovationScreen
import com.example.signsync.ui.theme.Screens.InstructionScreen
import com.example.signsync.ui.theme.Screens.LoginScreen
import com.example.signsync.ui.theme.Screens.SettingsScreen
import com.example.signsync.ui.theme.Screens.SignUpScreen
import com.example.signsync.ui.theme.Screens.UsernameScreen
import com.example.signsync.ui.theme.Screens.WelcomeScreen

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val USERNAME = "username" // 🆕 Required for name entry
    const val WELCOME = "welcome"
    const val ABOUT_US = "about_us"
    const val INNOVATION = "innovation"
    const val INSTRUCTION = "instruction"
    const val FEEDBACK = "feedback"
    const val SETTING = "setting"
    const val DETECTION = "detection"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // This instance is shared across all screens in the NavHost
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        // --- Authentication Flow ---
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(Routes.SIGNUP) {
            SignUpScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(Routes.USERNAME) {
            // Screen where user sets their name[cite: 5]
            UsernameScreen(navController = navController, authViewModel = authViewModel)
        }

        // --- Main App Flow ---
        composable(Routes.WELCOME) {
            // FIXED: Added authViewModel here so it can display the name[cite: 5]
            WelcomeScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(Routes.DETECTION) {
            DetectionScreen(navController = navController)
        }

        // --- Support & Info Screens ---
        composable(Routes.ABOUT_US) {
            AboutUsScreen(navController = navController)
        }

        composable(Routes.INNOVATION) {
            InnovationScreen(navController = navController)
        }

        composable(Routes.SETTING) {
            SettingsScreen(navController = navController)
        }

        composable(Routes.INSTRUCTION) {
            InstructionScreen(navController = navController)
        }

        composable(Routes.FEEDBACK) {
            FeedbackScreen(navController = navController, authViewModel = authViewModel)
        }
    }
}