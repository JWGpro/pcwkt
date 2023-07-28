package com.example.api

import kotlin.math.abs

class CellVector(val x: Int, val y: Int) {
    override fun toString(): String {
        return "$x,$y"
    }

    override fun equals(other: Any?): Boolean {
        return if (other is CellVector) {
            this.x == other.x && this.y == other.y
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    fun add(other: CellVector): CellVector {
        return CellVector(this.x + other.x, this.y + other.y)
    }

    fun subtract(other: CellVector): CellVector {
        return CellVector(this.x - other.x, this.y - other.y)
    }

    // "Manhattan distance"
    fun manDist(other: CellVector): Int {
        return abs(this.x - other.x) + abs(this.y - other.y)
    }
}