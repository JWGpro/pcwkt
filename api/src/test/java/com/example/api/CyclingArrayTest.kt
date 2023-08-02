package com.example.api

import org.junit.jupiter.api.Assertions.assertEquals

internal class CyclingArrayTest {

    @org.junit.jupiter.api.Test
    fun cycleForwardBackward() {
        val arr = CyclingArray(arrayOf(1, 2, 3))

        assertEquals(1, arr.current())

        assertEquals(2, arr.cycle())
        assertEquals(3, arr.cycle())
        assertEquals(1, arr.cycle())

        assertEquals(3, arr.cycle(backwards = true))
        assertEquals(2, arr.cycle(backwards = true))
        assertEquals(1, arr.cycle(backwards = true))
        assertEquals(3, arr.cycle(backwards = true))

    }

}