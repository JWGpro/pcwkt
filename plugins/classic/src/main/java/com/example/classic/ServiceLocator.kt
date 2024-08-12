package com.example.classic

import com.example.classic.ui.ActionMenu
import com.example.classic.ui.DeployMenu
import com.example.classic.ui.UnloadMenu

// This isn't really a service locator pattern but whatever
// Best I can come up with for now that isn't passing these around to everything
// TODO 2024-08-12: Maybe some kind of dependency injection (DI) framework? e.g. Dagger
//  I mean this is literal garbage right now
//  And DI is as simple as "passing these around"
//  I don't think Service Locator is intended for this use case. It's more for a low-level engine.
object ServiceLocator {

    lateinit var mapManager: MapManager
    lateinit var actionMenu: ActionMenu
    lateinit var unloadMenu: UnloadMenu
    lateinit var deployMenu: DeployMenu
    lateinit var turnManager: TurnManager

}