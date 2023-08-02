package com.example.api

class CyclingArray<T>(private val values: Array<T>) {
    private var index = 0

    fun current(): T {
        return values[index]
    }

    fun cycle(backwards: Boolean = false): T {
        val direction = if (backwards) -1 else 1

        // Modulo forces index to cycle within the array bounds
        index = (index + direction).mod(values.size)

        return current()
    }
}