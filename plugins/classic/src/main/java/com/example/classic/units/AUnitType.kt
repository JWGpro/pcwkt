package com.example.classic.units

import com.example.classic.Assets
import com.example.classic.MovementTypes
import com.example.classic.Team

enum class AUnitType(
    val spritePathMap: Map<Team, String>,
    val price: Int,
    val moveRange: Int,
    val movementType: MovementTypes,
    val boardCap: Int = 0,
    val boardable: Array<AUnitType>? = null
) {
    INFANTRY(
        Assets.Textures.INFANTRY.paths,
        1000,
        3,
        MovementTypes.INF
    ),
    APC(
        Assets.Textures.APC.paths,
        4000,
        6,
        MovementTypes.TRACK,
        boardCap = 1,
        boardable = arrayOf(INFANTRY)
    ),
}