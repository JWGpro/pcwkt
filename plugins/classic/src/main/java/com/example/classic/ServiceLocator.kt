package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage

// Best I can come up with for now that isn't passing these around to everything
object ServiceLocator {

    lateinit var gameStage: Stage
    lateinit var assetManager: AssetManager
    lateinit var mapManager: MapManager

    fun init(gameStage: Stage, assetManager: AssetManager, mapManager: MapManager) {
        this.gameStage = gameStage
        this.assetManager = assetManager
        this.mapManager = mapManager
    }
}