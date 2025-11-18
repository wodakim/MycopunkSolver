package com.mycopunk.solver.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import com.mycopunk.solver.model.Axial
import com.mycopunk.solver.model.Placement
import com.mycopunk.solver.model.Shape
import com.mycopunk.solver.utils.HexUtils
import kotlin.math.*

@Composable
fun HexGrid(
    occupied: Set<Axial>,
    placements: List<Placement>,
    currentDrawing: Set<Axial>,
    currentColor: Color,
    onCellTapped: (Axial) -> Unit,
    modifier: Modifier = Modifier
) {
    val allCells = HexUtils.getAllHexes()

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                val axial = pixelToAxial(offset.x, offset.y, size.width, size.height)
                if (axial in allCells) onCellTapped(axial)
            }
        }
    ) {
        val width = size.width
        val height = size.height
        val centerX = width / 2f
        val centerY = height / 2f

        // Dessin des hexagones
        allCells.forEach { axial ->
            val (x, y) = HexUtils.axialToPixel(centerX, centerY, axial)
            val placement = placements.firstOrNull { p -> axial - p.offset in p.shape.cells }
            val color = when {
                axial in occupied -> Color(0xFF444444)
                placement != null -> placement.shape.color
                axial in currentDrawing -> currentColor.copy(alpha = 0.7f)
                else -> Color(0xFF222233)
            }

            drawHexagon(x, y, HexUtils.HEX_SIZE * 0.95f, color, Color(0xFF00FFFF).copy(alpha = 0.3f))
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawHexagon(
    centerX: Float,
    centerY: Float,
    size: Float,
    fillColor: Color,
    strokeColor: Color
) {
    val path = Path()
    for (i in 0..6) {
        // The angle calculation should use floating point numbers
        val angle = (Math.PI / 3.0 * i).toFloat() // Ensure float calculation
        val x = centerX + size * kotlin.math.cos(angle)
        val y = centerY + size * kotlin.math.sin(angle) * 0.866f // flat-top
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = fillColor)
    drawPath(path, color = strokeColor, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
}

private fun pixelToAxial(x: Float, y: Float, width: Float, height: Float): Axial {
    val px = (x - width / 2f) / HexUtils.HEX_SIZE
    val py = (y - height / 2f) / HexUtils.HEX_SIZE
    val q = (sqrt(3f)/3f * px - 1f/3f * py)
    val r = (2f/3f * py)
    val s = -q - r
    var qq = round(q).toInt()
    var rr = round(r).toInt()
    var ss = round(s).toInt()
    if (qq + rr + ss != 0) {
        val qDiff = abs(qq - q)
        val rDiff = abs(rr - r)
        val sDiff = abs(ss - s)
        if (qDiff > rDiff && qDiff > sDiff) {
            qq = -rr - ss
        } else if (rDiff > sDiff) {
            rr = -qq - ss
        } else {
            ss = -qq - rr
        }
    }
    return Axial(qq, rr)
}