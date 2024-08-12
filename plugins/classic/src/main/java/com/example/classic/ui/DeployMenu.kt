package com.example.classic.ui

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.example.classic.Assets
import com.example.classic.ServiceLocator
import com.example.classic.selectionstate.DeployingState
import com.example.classic.terrains.Property
import com.example.classic.units.AUnitType

class DeployMenu(
    private val uiStage: Stage,
    assetManager: AssetManager
) {
    private val container = Table()
    private val turnManager = ServiceLocator.turnManager
    private val mapManager = ServiceLocator.mapManager
    private var deployingState: DeployingState? = null
    private var property: Property? = null
    private val deployButtons = AUnitType.values().associateWith {
        val button = TextButton(it.title, assetManager.get<Skin>(Assets.Skins.DEFAULT.path))
        button.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                deploy(it)
            }
        })
        button
    }

    init {
        uiStage.addActor(container)
        container.setFillParent(true)
        container.top()
        container.left()
        container.pad(10f)
    }

    fun deploy(unitType: AUnitType) {
        mapManager.makeUnit(property!!.gridRef.vector, turnManager.teamsPlaying.current(), unitType)
        clear()

        // TODO 2024-08-12: Let's try DI first (see ServiceLocator comments)
//        replayManager.append(
//            UnitActionCommand(actionMenu.moveCommand!!, waitCommand)
//        )
//
//        selectionStateManager.defaultState()
    }

    fun clear() {
        container.clearChildren()
    }

    fun show(deployingState: DeployingState, property: Property) {
        this.deployingState = deployingState
        this.property = property

        // Evaluate visible buttons
        property.unitsDeployable!!.forEach { unitType ->
            val unitButton = deployButtons[unitType]

            container.add(unitButton)
            container.row()
        }
    }
}
