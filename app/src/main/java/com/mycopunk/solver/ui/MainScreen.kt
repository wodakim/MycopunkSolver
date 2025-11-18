package com.mycopunk.solver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycopunk.solver.model.Axial
import com.mycopunk.solver.model.Placement
import com.mycopunk.solver.model.Shape
import com.mycopunk.solver.solver.GridSolver
import com.mycopunk.solver.utils.HexUtils
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var occupied by remember { mutableStateOf(setOf<Axial>()) }
    var shapes by remember { mutableStateOf(listOf<Shape>()) }
    var currentDrawing by remember { mutableStateOf(setOf<Axial>()) }
    var currentColor by remember { mutableStateOf(Color(Random.nextLong(0xFF000000, 0xFFFFFFFF)).copy(alpha = 0.9f)) }
    var mode by remember { mutableStateOf<Mode>(Mode.DRAW_SHAPE) }
    var solutions by remember { mutableStateOf(listOf<List<Placement>>()) }
    var isSolving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val solver = remember { GridSolver() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A1A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("MYCOPUNK GRID SOLVER PRO", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.Cyan)

        Spacer(modifier = Modifier.height(20.dp))

        // La grille principale
        Box(modifier = Modifier.size(500.dp)) {
            HexGrid(
                occupied = occupied,
                placements = if (solutions.isNotEmpty()) solutions[0] else emptyList(),
                currentDrawing = currentDrawing,
                currentColor = currentColor,
                onCellTapped = { cell ->
                    when (mode) {
                        Mode.DRAW_SHAPE -> {
                            currentDrawing = if (cell in currentDrawing) currentDrawing - cell else currentDrawing + cell
                        }
                        Mode.OCCUPIED -> {
                            occupied = if (cell in occupied) occupied - cell else occupied + cell
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Boutons de contrôle
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { currentColor = Color(Random.nextLong(0xFF000000, 0xFFFFFFFF)).copy(alpha = 0.9f) }) {
                Text("Nouvelle couleur")
            }
            Button(onClick = {
                if (currentDrawing.isNotEmpty()) {
                    shapes = shapes + Shape(currentDrawing.toList(), currentColor)
                    currentDrawing = emptySet()
                }
            }, enabled = currentDrawing.isNotEmpty()) {
                Text("✓ Ajouter Shape")
            }
            Button(onClick = { currentDrawing = emptySet(); occupied = emptySet(); shapes = emptyList(); solutions = emptyList() }) {
                Text("Clear tout")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Text("Mode : ", color = Color.White)
            Text(if (mode == Mode.DRAW_SHAPE) "Dessin Shape" else "Marquer Occupé (gris)", color = if (mode == Mode.DRAW_SHAPE) Color.Cyan else Color.Gray)
            Switch(checked = mode == Mode.OCCUPIED, onCheckedChange = { mode = if (it) Mode.OCCUPIED else Mode.DRAW_SHAPE })
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Liste des shapes ajoutées
        Text("Shapes ajoutées (${shapes.size})", color = Color.Magenta, fontSize = 20.sp)
        LazyRow {
            items(shapes) { shape ->
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(4.dp)
                        .background(shape.color)
                        .border(2.dp, Color.White)
                        .clickable { shapes = shapes - shape }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (shapes.isEmpty()) return@Button
                isSolving = true
                solutions = emptyList()
                coroutineScope.launch {
                    solver.solve(occupied, shapes, allowRotations = true, maxSolutions = 20)
                        .collect { solution ->
                            solutions = solutions + listOf(solution)
                        }
                    isSolving = false
                }
            },
            enabled = !isSolving && shapes.isNotEmpty(),
            modifier = Modifier.height(60.dp)
        ) {
            if (isSolving) CircularProgressIndicator(color = Color.White) else Text("SOLVE (trouve toutes les solutions)", fontSize = 20.sp)
        }

        if (solutions.isNotEmpty()) {
            Text("${solutions.size} solution(s) trouvée(s) en un clin d'œil !", color = Color.Green, fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Carousel des solutions
        if (solutions.isNotEmpty()) {
            Text("Solutions trouvées :", color = Color.Cyan, fontSize = 24.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(solutions) { solution ->
                    Box(modifier = Modifier.size(400.dp)) {
                        HexGrid(
                        occupied = occupied,
                        placements = solution,
                        currentDrawing = emptySet(),
                        currentColor = Color.Transparent,
                        onCellTapped = {}
                    )
                }
            }
        }
    }
}

enum class Mode { DRAW_SHAPE, OCCUPIED }