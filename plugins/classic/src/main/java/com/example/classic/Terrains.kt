package com.example.classic

enum class Terrains(
    val title: String,
    val path: String,
    val defence: Int,
    val moveCosts: Map<MovementTypes, Int>
) {
    SEA(
        "Sea",
        "sea.png",
        0,
        mapOf(
            MovementTypes.AIR to 1,
            MovementTypes.SHIP to 1,
            MovementTypes.LANDER to 1,
        )
    ),
    REEF(
        "Reef",
        "reef.png",
        0,
        mapOf(
            MovementTypes.AIR to 1,
            MovementTypes.SHIP to 2,
            MovementTypes.LANDER to 2
        )
    ),
    ROAD(
        "Road",
        "road.png",
        0,
        mapOf(
            MovementTypes.INF to 1,
            MovementTypes.MECH to 1,
            MovementTypes.TYRE to 1,
            MovementTypes.TRACK to 1,
            MovementTypes.AIR to 1
        )
    ),
    PLAIN(
        "Plain",
        "plain.png",
        1,
        mapOf(
            MovementTypes.INF to 1,
            MovementTypes.MECH to 1,
            MovementTypes.TYRE to 2,
            MovementTypes.TRACK to 1,
            MovementTypes.AIR to 1
        )
    ),
    FOREST(
        "Forest",
        "forest.png",
        2,
        mapOf(
            MovementTypes.INF to 1,
            MovementTypes.MECH to 1,
            MovementTypes.TYRE to 3,
            MovementTypes.TRACK to 2,
            MovementTypes.AIR to 1
        )
    ),
    MOUNTAIN(
        "Mountain",
        "mountain.png",
        4,
        mapOf(
            MovementTypes.INF to 2,
            MovementTypes.MECH to 1,
            MovementTypes.AIR to 1
        )
    ),
    RIVER(
        "River",
        "river.png",
        0,
        mapOf(
            MovementTypes.INF to 2,
            MovementTypes.MECH to 1,
            MovementTypes.AIR to 1
        )
    ),
    SHOAL(
        "Shoal",
        "shoal.png",
        0,
        mapOf(
            MovementTypes.INF to 1,
            MovementTypes.MECH to 1,
            MovementTypes.TYRE to 2,
            MovementTypes.TRACK to 1,
            MovementTypes.AIR to 1,
            MovementTypes.LANDER to 1
        )
    )
}