package com.example.signsync



import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.signsync.R

// --- Colors and Fonts (Centralized) ---

// Light blue background color (hex #B3E5FC)
val LightBlueBackground = Color(0xFFB3E5FC)
// Transparent White for the card (50% alpha)
val TransparentCardColor = Color.White.copy(alpha = 0.8f)
// A contrasting darker blue for text and icons
val DarkBlueAccent = Color(0xFF0277BD)
// Dark Blue for TopBar and text
val DarkBlue = Color(0xFF003366)
// Glassy white for buttons
val GlassyButtonColor = Color(0xCCFFFFFF)
// Pure White for light text
val WhiteText = Color(0xFFFFFFFF)

// Placeholder for Poppins font (NOTE: R.font.poppins_medium must be available in your resources)
val MediumPoppins = FontFamily(Font(R.font.poppins_medium))