package com.example.classic.serial

import com.example.classic.Team
import com.example.classic.units.AUnitType
import kotlinx.serialization.Serializable

@Serializable
class AUnitSerial(val type: AUnitType, val team: Team) {
}