package com.mycopunk.solver.model

import androidx.compose.ui.graphics.Color

data class Shape(
    val cells: List<Axial>,
    val color: Color = Color(0xFF00FFFF).copy(alpha = 0.9f)
)