package com.mycopunk.solver.solver

import com.mycopunk.solver.model.Axial
import com.mycopunk.solver.model.Placement
import com.mycopunk.solver.model.Shape
import com.mycopunk.solver.utils.HexUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class GridSolver {
    private val allCells = HexUtils.getAllHexes()

    fun solve(
        occupied: Set<Axial>,
        shapes: List<Shape>,
        allowRotations: Boolean = true,
        maxSolutions: Int = 50
    ): Flow<List<Placement>> {
        val channel = Channel<List<Placement>>(Channel.UNLIMITED)
        val mutableOccupied = occupied.toMutableSet()
        val sortedShapes = shapes.sortedByDescending { it.cells.size }

        fun backtrack(index: Int, current: MutableList<Placement>) {
            if (current.size >= maxSolutions) return
            if (index == sortedShapes.size) {
                channel.trySend(current.toList())
                return
            }

            val shape = sortedShapes[index]
            val rotations = if (allowRotations) HexUtils.getAllUniqueRotations(shape) else listOf(shape)

            for (rot in rotations) {
                for (offset in allCells) {
                    val placedCells = rot.cells.map { it + offset }
                    if (placedCells.all { it in allCells } && placedCells.none { it in mutableOccupied }) {
                        placedCells.forEach { mutableOccupied.add(it) }
                        current.add(Placement(rot, offset))
                        backtrack(index + 1, current)
                        placedCells.forEach { mutableOccupied.remove(it) }
                        current.removeLast()
                    }
                }
            }
        }

        Thread { 
            backtrack(0, mutableListOf())
            channel.close()
        }.start()

        return channel.receiveAsFlow()
    }
}