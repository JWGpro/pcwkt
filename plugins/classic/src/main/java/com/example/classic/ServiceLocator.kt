package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.example.classic.selectionstate.SelectionStateManager

// Best I can come up with for now that isn't passing these around to everything
object ServiceLocator {

    lateinit var gameStage: Stage
    lateinit var assetManager: AssetManager
    lateinit var mapManager: MapManager
    lateinit var selectionStateManager: SelectionStateManager

    fun init(gameStage: Stage, assetManager: AssetManager, mapManager: MapManager) {
        this.gameStage = gameStage
        this.assetManager = assetManager
        this.mapManager = mapManager
        // TODO: This is starting to get crap
        this.selectionStateManager = SelectionStateManager()
    }
}