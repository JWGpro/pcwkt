package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.example.classic.ui.ActionMenu

// This isn't really a service locator pattern but whatever
// Best I can come up with for now that isn't passing these around to everything
object ServiceLocator {

    lateinit var gameStage: Stage
    lateinit var assetManager: AssetManager
    lateinit var mapManager: MapManager
    lateinit var actionMenu: ActionMenu
    lateinit var turnManager: TurnManager

}