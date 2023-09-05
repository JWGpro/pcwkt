package com.example.classic.units

import com.example.classic.Assets
import com.example.classic.MovementTypes
import com.example.classic.Team

val GROUND_UNITS = arrayOf(
    AUnitType.INFANTRY,
    AUnitType.APC
)

interface WeaponType {
    val title: String
    val damageMap: Map<AUnitType, Float>
    val isDirect: Boolean
    val minRange: Int
    val maxRange: Int
}

enum class AUnitType(
    val spritePathMap: Map<Team, String>,
    val price: Int,
    val moveRange: Int,
    val movementType: MovementTypes,
    val boardCap: Int = 0,
    val boardable: Array<AUnitType>? = null,
    val canCapture: Boolean = false,
    // Function call allows references to all enum values before their initialisation
    val getWeapons: () -> Array<WeaponType>? = { null }
) {
    INFANTRY(
        Assets.Textures.INFANTRY.paths,
        1000,
        3,
        MovementTypes.INF,
        canCapture = true,
        getWeapons = {
            arrayOf(
                object : WeaponType {
                    override val title = "Rifle"
                    override val damageMap = mapOf(
                        INFANTRY to 60f,
                        APC to 20f,
                    )
                    override val isDirect = true
                    override val minRange = 1
                    override val maxRange = 1
                }
            )
        }
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