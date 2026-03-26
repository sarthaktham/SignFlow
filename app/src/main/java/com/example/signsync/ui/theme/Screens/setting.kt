package com.example.signsync.ui.theme.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.signsync.DarkBlue
import com.example.signsync.GlassyButtonColor
import com.example.signsync.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var isDarkMode by remember { mutableStateOf(false) }
    var highAccuracy by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                "Preferences",
                modifier = Modifier.padding(16.dp),
                color = DarkBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            SettingToggle(
                title = "Dark Mode",
                icon = Icons.Default.DarkMode,
                state = isDarkMode,
                onCheckedChange = { isDarkMode = it }
            )

            SettingToggle(
                title = "High Accuracy Mode",
                icon = Icons.Default.Speed,
                state = highAccuracy,
                onCheckedChange = { highAccuracy = it }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                "App Info",
                modifier = Modifier.padding(16.dp),
                color = DarkBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            SettingItem(title = "App Version", subtitle = "1.0.4 (Stable)", icon = Icons.Default.Info)
            SettingItem(title = "Privacy Policy", subtitle = "Read our terms", icon = Icons.Default.PrivacyTip)
        }
    }
}

@Composable
fun SettingToggle(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, state: Boolean, onCheckedChange: (Boolean) -> Unit) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Medium) },
        leadingContent = { Icon(icon, contentDescription = null, tint = DarkBlue) },
        trailingContent = {
            Switch(checked = state, onCheckedChange = onCheckedChange)
        }
    )
}

@Composable
fun SettingItem(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Medium) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, contentDescription = null, tint = DarkBlue) },
        modifier = Modifier.clickable { /* Action */ }
    )
}