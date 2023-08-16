package com.example.classic.serial

import kotlinx.serialization.Serializable

@Serializable
class GridRefSerial(val unit: AUnitSerial?, val terrain: TerrainSerial)