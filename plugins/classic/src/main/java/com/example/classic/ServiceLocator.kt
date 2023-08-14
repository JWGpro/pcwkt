package com.example.classic

import com.example.classic.ui.ActionMenu
import com.example.classic.ui.UnloadMenu

// This isn't really a service locator pattern but whatever
// Best I can come up with for now that isn't passing these around to everything
object ServiceLocator {

    lateinit var mapManager: MapManager
    lateinit var actionMenu: ActionMenu
    lateinit var unloadMenu: UnloadMenu
    lateinit var turnManager: TurnManager

}