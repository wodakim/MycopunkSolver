package com.mycopunk.solver.model

data class Axial(val q: Int, val r: Int) {
    operator fun plus(other: Axial) = Axial(q + other.q, r + other.r)
    operator fun minus(other: Axial) = Axial(q - other.q, r - other.r)
}