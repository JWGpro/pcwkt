package com.example.classic.ui

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.example.classic.Assets
import com.example.classic.MapManager
import com.example.classic.selectionstate.SelectionStateManager
import com.example.classic.units.AUnit

class ActionMenu(
    uiStage: Stage,
    assetManager: AssetManager,
    private val mapManager: MapManager,
    private val selectionStateManager: SelectionStateManager
) {

    // Front-loading everything. Trying to avoid dynamic allocation
    private val actionTable = Table()
    private val actionButtons = Actions.values().associateWith {
        val button = TextButton(it.title, assetManager.get<Skin>(Assets.Skins.DEFAULT.path))
        button.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                it.action(this@ActionMenu)
            }
        })
        button
    }
    private var unit: AUnit? = null

    // TODO: Command was used here for replays
    private enum class Actions(
        val title: String,
        val isShowable: (unit: AUnit) -> Boolean,
        val action: (menu: ActionMenu) -> Unit
    ) {
        // The actions appear in this order.
        BOARD("Board", {
            // This is mutually exclusive with WAIT. You can't WAIT on a transport.
            false
        }, {}),
        CAPTURE("Capture", { false }, {}),
        ATTACK("Attack", { false }, {}),
        UNLOAD("Unload", { false }, {}),
        HOLD("Hold", { false }, {}),
        WAIT("Wait", { !BOARD.isShowable(it) }, {
            // TODO: Disabled until turn cycling is implemented
//            it.unit?.waitHere()

            // End move (not end turn)
            it.clear()
            it.mapManager.clearRanges()

            it.selectionStateManager.defaultState()
        }),
    }

    init {
        uiStage.addActor(actionTable)
        actionTable.setFillParent(true)
        actionTable.top()
        actionTable.left()
        actionTable.pad(10f)
    }

    fun show(unit: AUnit) {
        this.unit = unit

        // Evaluate potential actions
        Actions.values().forEach { action ->
            val button = actionButtons[action]
            if (action.isShowable(unit)) {
                actionTable.add(button)
                actionTable.row()
            }
        }
    }

    fun clear() {
        this.unit = null
        actionTable.clearChildren()
    }
}