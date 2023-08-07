package com.example.classic.serial

import com.example.classic.Terrains
import kotlinx.serialization.Serializable

@Serializable
class GridRefSerial(val unit: AUnitSerial?, val terrain: Terrains)