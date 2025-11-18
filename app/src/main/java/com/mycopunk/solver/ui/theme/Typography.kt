package com.mycopunk.solver.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// On part de la typo par défaut de Material3 et on override seulement ce qu'on veut en néon
val MycopunkTypography = Typography(
    // Titres principaux en cyan néon bien flashy
    headlineLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        color = Color(0xFF00FFFF)
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = Color(0xFF00FFFF)
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = Color(0xFFFF00FF) // magenta pour les titres secondaires
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        color = Color(0xFFFF00FF)
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color(0xFFE0E0E0)
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.White
    )
    // Tous les autres (bodyMedium, labelSmall, etc.) restent aux valeurs Material3 par défaut
)