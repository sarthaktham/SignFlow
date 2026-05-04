package com.example.signsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.signsync.ui.theme.Screens.AboutUsScreen
import com.example.signsync.ui.theme.Screens.DetectionScreen
import com.example.signsync.ui.theme.Screens.InnovationScreen // New Import
import com.example.signsync.ui.theme.Screens.InstructionScreen
import com.example.signsync.ui.theme.Screens.LoginScreen
import com.example.signsync.ui.theme.Screens.SettingsScreen
import com.example.signsync.ui.theme.Screens.WelcomeScreen

// --- Routes ---
/**
 * Defines the navigation constants (route strings) for the entire application.
 */
object Routes {
    const val LOGIN = "login"
    const val WELCOME = "welcome"
    const val ABOUT_US = "about_us"
    const val INNOVATION = "innovation" // <-- NEW ROUTE

    const val INSTRUCTION = "instruction"

    const val FEEDBACK = "feedback"
    const val SETTING = "setting"
    const val DETECTION = "detection"

}

// --- Main Activity ---

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Assume your app's main theme is applied here
            MaterialTheme {
                AppNavigation() // Start the navigation host
            }
        }
    }
}

// --- Navigation Composable ---

/**
 * Manages the navigation flow using the NavHost.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(Routes.WELCOME) {
            WelcomeScreen(navController = navController)
        }
        composable(Routes.ABOUT_US) {
            AboutUsScreen(navController = navController)
        }
        composable(Routes.INNOVATION) { // <-- NEW COMPOSABLE
            InnovationScreen(navController = navController)
        }
        composable(Routes.SETTING) {
            SettingsScreen(navController = navController)
        }
        composable(Routes.INSTRUCTION) {
            InstructionScreen(navController = navController)
        }

        composable(Routes.DETECTION) {
            DetectionScreen(navController = navController)
        }
    }
}