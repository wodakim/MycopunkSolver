package com.mycopunk.solver.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF00FFFF), // cyan néon
    secondary = androidx.compose.ui.graphics.Color(0xFFFF00FF), // magenta
    background = androidx.compose.ui.graphics.Color(0xFF0A0A1A),
    surface = androidx.compose.ui.graphics.Color(0xFF1A1A2E),
    onPrimary = androidx.compose.ui.graphics.Color.Black,
    onBackground = androidx.compose.ui.graphics.Color(0xFFE0E0E0),
    onSurface = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
)

@Composable
fun MycopunkSolverTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = MycopunkTypography,
        content = content
    )
}