package com.example.classic.serial

import com.example.classic.Team
import com.example.classic.terrains.TerrainType
import kotlinx.serialization.Serializable

@Serializable
class TerrainSerial(val type: TerrainType, val team: Team?) {
}