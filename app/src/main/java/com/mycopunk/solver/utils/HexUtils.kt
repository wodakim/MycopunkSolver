package com.mycopunk.solver.utils

import com.mycopunk.solver.model.Axial
import com.mycopunk.solver.model.Shape
import kotlin.math.*

object HexUtils {
    const val HEX_SIZE = 46f

    fun getAllHexes(): Set<Axial> {
        val set = mutableSetOf<Axial>()
        for (r in -3..3) {
            val qMin = maxOf(-3, -3 - r)
            val qMax = minOf(3, 3 - r)
            for (q in qMin..qMax) set.add(Axial(q, r))
        }
        return set
    }

    fun axialToPixel(centerX: Float, centerY: Float, a: Axial): Pair<Float, Float> {
        val x = centerX + HEX_SIZE * (sqrt(3f) * a.q + sqrt(3f)/2f * a.r)
        val y = centerY + HEX_SIZE * (3f/2f * a.r)
        return x to y
    }

    fun rotate60(a: Axial): Axial = Axial(-a.r, a.q + a.r)

    fun getAllUniqueRotations(shape: Shape): List<Shape> {
        val seen = mutableSetOf<List<Axial>>()
        var current = shape.cells
        for (i in 0 until 6) {
            val minQ = current.minOf { it.q }
            val minR = current.minOf { it.r }
            val normalized = current.map { Axial(it.q - minQ, it.r - minR) }.sortedBy { it.q * 31 + it.r }
            seen.add(normalized)
            current = current.map { rotate60(it) }
        }
        return seen.map { Shape(it, shape.color) }
    }
}