package com.example.classic.terrains

import com.example.classic.MapManager
import com.example.classic.Team
import com.example.classic.units.AUnit

const val MAX_CAPTURE_STRENGTH = 20

class Property(
    val gridRef: MapManager.GridReference,
    val type: TerrainType,
    var team: Team,
    private val mapManager: MapManager
) : Terrain {

    override val title = type.title
    override val paths = type.paths
    override val defence = type.defence
    override val moveCosts = type.moveCosts
    override val isProperty = true
    override val repairsUnits = type.repairsUnits
    override val unitsDeployable = type.unitsDeployable

    var captureStrength = MAX_CAPTURE_STRENGTH

    fun capture(unit: AUnit) {
        captureStrength -= unit.getStrength()
        if (captureStrength <= 0) {
            switchToTeam(unit.team)
        } else {
            // TODO: Obviously some real visual feedback
            println("Remaining capture strength: $captureStrength")
        }
    }

    fun switchToTeam(team: Team) {
        this.team = team
        mapManager.changePropertyTeam(this, team)
        captureStrength = MAX_CAPTURE_STRENGTH
    }
}