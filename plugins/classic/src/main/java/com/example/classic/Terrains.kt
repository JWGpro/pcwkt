package com.example.classic

enum class Terrains(
    val title: String,
    val path: String,
    val defence: Int,
    val moveCosts: Map<MoveTypes, Int>
) {
    SEA(
        "Sea",
        "sea.png",
        0,
        mapOf(
            MoveTypes.AIR to 1,
            MoveTypes.SHIP to 1,
            MoveTypes.LANDER to 1,
        )
    ),
}