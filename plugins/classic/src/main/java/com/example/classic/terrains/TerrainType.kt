package com.example.classic.terrains

import com.example.classic.MovementTypes
import com.example.classic.Team
import com.example.classic.units.AUnitType
import com.example.classic.units.GROUND_UNITS

enum class TerrainType(
    override val title: String,
    override val paths: Map<Team, TilePath>,
    override val defence: Int,
    override val moveCosts: Map<MovementTypes, Int>,
    override val isProperty: Boolean = false,
    override val repairsUnits: Array<AUnitType>? = null,
    override val unitsDeployable: Array<AUnitType>? = null,
) : Terrain {
    SEA(
        "Sea",
        mapOf(Team.NEUTRAL to TilePath.SEA),
        0,
        mapOf(
            MovementTypes.AIR to 1,
            MovementTypes.SHIP to 1,
            MovementTypes.LANDER to 1,
        )
    ),
    REEF(
        "Reef",
        mapOf(Team.NEUTRAL to TilePath.REEF),
        0,
        mapOf(
            MovementTypes.AIR to 1,
            MovementTypes.SHIP to 2,
            MovementTypes.LANDER to 2
        )
    ),
    ROAD(
        "Road",
        mapOf(Team.NEUTRAL to TilePath.ROAD),
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
        mapOf(Team.NEUTRAL to TilePath.PLAIN),
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
        mapOf(Team.NEUTRAL to TilePath.FOREST),
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
        mapOf(Team.NEUTRAL to TilePath.MOUNTAIN),
        4,
        mapOf(
            MovementTypes.INF to 2,
            MovementTypes.MECH to 1,
            MovementTypes.AIR to 1
        )
    ),
    RIVER(
        "River",
        mapOf(Team.NEUTRAL to TilePath.RIVER),
        0,
        mapOf(
            MovementTypes.INF to 2,
            MovementTypes.MECH to 1,
            MovementTypes.AIR to 1
        )
    ),
    SHOAL(
        "Shoal",
        mapOf(Team.NEUTRAL to TilePath.SHOAL),
        0,
        mapOf(
            MovementTypes.INF to 1,
            MovementTypes.MECH to 1,
            MovementTypes.TYRE to 2,
            MovementTypes.TRACK to 1,
            MovementTypes.AIR to 1,
            MovementTypes.LANDER to 1
        )
    ),
    FACTORY(
        "Factory",
        mapOf(
            Team.NEUTRAL to TilePath.FACTORY_NEUTRAL,
            Team.RED to TilePath.FACTORY_RED,
            Team.BLUE to TilePath.FACTORY_BLUE,
        ),
        3,
        mapOf(
            MovementTypes.INF to 1,
            MovementTypes.MECH to 1,
            MovementTypes.TYRE to 2,
            MovementTypes.TRACK to 1,
            MovementTypes.AIR to 1
        ),
        isProperty = true,
        repairsUnits = GROUND_UNITS,
        unitsDeployable = GROUND_UNITS
    )
}