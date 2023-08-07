package com.example.classic.units

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.example.classic.MapManager
import com.example.classic.Team
import com.example.classic.TurnManager

class AUnitFactory(
    private val assetManager: AssetManager,
    private val gameStage: Stage,
    private val turnManager: TurnManager,
    private val mapManager: MapManager,
) {

    fun make(x: Int, y: Int, team: Team, type: AUnitType): AUnit {
        val unit = AUnit(x, y, team, type, assetManager, gameStage, mapManager)

        turnManager.teamUnits[unit.team]?.add(unit)

        return unit
    }
}