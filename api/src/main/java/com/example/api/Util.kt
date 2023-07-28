package com.example.api

object Util {
    // TODO: Parameterise
    fun clampMin(n: Int, minimum: Int): Int {
        return if (n < minimum) minimum else n
    }

    fun clampMax(n: Int, maximum: Int): Int {
        return if (n > maximum) maximum else n
    }
}