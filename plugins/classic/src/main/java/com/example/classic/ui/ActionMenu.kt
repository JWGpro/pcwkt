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
import com.example.classic.ReplayManager
import com.example.classic.commands.MoveCommand
import com.example.classic.commands.action.UnitActionCommand
import com.example.classic.commands.action.WaitCommand
import com.example.classic.selectionstate.SelectionStateManager

class ActionMenu(
    uiStage: Stage,
    assetManager: AssetManager,
    private val mapManager: MapManager,
    private val selectionStateManager: SelectionStateManager,
    private val replayManager: ReplayManager
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
    private var moveCommand: MoveCommand? = null

    private enum class Actions(
        val title: String,
        val isShowable: (moveCommand: MoveCommand) -> Boolean,
        val action: (actionMenu: ActionMenu) -> Unit
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
        WAIT("Wait", { moveCommand ->
            !BOARD.isShowable(moveCommand)
        }, { actionMenu ->

            val waitCommand = WaitCommand(actionMenu.moveCommand!!.unit)
            waitCommand.execute()

            actionMenu.replayManager.append(
                UnitActionCommand(actionMenu.moveCommand!!, waitCommand)
            )

            // Finish this move
            actionMenu.clear()
            actionMenu.mapManager.clearRanges()

            actionMenu.selectionStateManager.defaultState()
        }),
    }

    init {
        uiStage.addActor(actionTable)
        actionTable.setFillParent(true)
        actionTable.top()
        actionTable.left()
        actionTable.pad(10f)
    }

    fun show(moveCommand: MoveCommand) {
        this.moveCommand = moveCommand

        // Evaluate potential actions
        Actions.values().forEach { action ->
            val button = actionButtons[action]
            if (action.isShowable(moveCommand)) {
                actionTable.add(button)
                actionTable.row()
            }
        }
    }

    fun clear() {
        this.moveCommand = null
        actionTable.clearChildren()
    }
}