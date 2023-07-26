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
    REEF(
        "Reef",
        "reef.png",
        0,
        mapOf(
            MoveTypes.AIR to 1,
            MoveTypes.SHIP to 2,
            MoveTypes.LANDER to 2
        )
    ),
    ROAD(
        "Road",
        "road.png",
        0,
        mapOf(
            MoveTypes.INF to 1,
            MoveTypes.MECH to 1,
            MoveTypes.TYRE to 1,
            MoveTypes.TRACK to 1,
            MoveTypes.AIR to 1
        )
    ),
    PLAIN(
        "Plain",
        "plain.png",
        1,
        mapOf(
            MoveTypes.INF to 1,
            MoveTypes.MECH to 1,
            MoveTypes.TYRE to 2,
            MoveTypes.TRACK to 1,
            MoveTypes.AIR to 1
        )
    ),
    FOREST(
        "Forest",
        "forest.png",
        2,
        mapOf(
            MoveTypes.INF to 1,
            MoveTypes.MECH to 1,
            MoveTypes.TYRE to 3,
            MoveTypes.TRACK to 2,
            MoveTypes.AIR to 1
        )
    ),
    MOUNTAIN(
        "Mountain",
        "mountain.png",
        4,
        mapOf(
            MoveTypes.INF to 2,
            MoveTypes.MECH to 1,
            MoveTypes.AIR to 1
        )
    ),
    RIVER(
        "River",
        "river.png",
        0,
        mapOf(
            MoveTypes.INF to 2,
            MoveTypes.MECH to 1,
            MoveTypes.AIR to 1
        )
    ),
    SHOAL(
        "Shoal",
        "shoal.png",
        0,
        mapOf(
            MoveTypes.INF to 1,
            MoveTypes.MECH to 1,
            MoveTypes.TYRE to 2,
            MoveTypes.TRACK to 1,
            MoveTypes.AIR to 1,
            MoveTypes.LANDER to 1
        )
    )
}