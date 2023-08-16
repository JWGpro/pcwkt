package com.example.classic.terrains

import com.example.classic.MovementTypes
import com.example.classic.Team
import com.example.classic.units.AUnitType

// TODO: This is ugly. Composition? "PropertyBehaviour"?
interface Terrain {

    val title: String
    val paths: Map<Team, TilePath>
    val defence: Int
    val moveCosts: Map<MovementTypes, Int>
    val isProperty: Boolean
    val repairsUnits: Array<AUnitType>?
    val unitsDeployable: Array<AUnitType>?

}