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
import com.example.classic.commands.action.BoardCommand
import com.example.classic.commands.action.CaptureCommand
import com.example.classic.commands.action.UnitActionCommand
import com.example.classic.commands.action.WaitCommand
import com.example.classic.selectionstate.MovedState
import com.example.classic.selectionstate.SelectionStateManager
import com.example.classic.selectionstate.TargetingState
import com.example.classic.selectionstate.UnloadingState
import com.example.classic.terrains.Property

class ActionMenu(
    private val uiStage: Stage,
    assetManager: AssetManager,
    private val mapManager: MapManager,
    private val selectionStateManager: SelectionStateManager,
    private val replayManager: ReplayManager,
    private val unloadMenu: UnloadMenu,
    private val targetingUI: TargetingUI
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
    private var movedState: MovedState? = null
    private var moveCommand: MoveCommand? = null

    init {
        uiStage.addActor(actionTable)
        actionTable.setFillParent(true)
        actionTable.top()
        actionTable.left()
        actionTable.pad(10f)
    }

    private enum class Actions(
        val title: String,
        val isShowable: (actionMenu: ActionMenu, moveCommand: MoveCommand) -> Boolean,
        val action: (actionMenu: ActionMenu) -> Unit
    ) {
        // The actions appear in this order.

        BOARD("Board", { actionMenu, moveCommand ->
            // This is mutually exclusive with WAIT. You can't WAIT (or HOLD) on a transport.
            val destination = actionMenu.mapManager.toGridRef(moveCommand.path.route.last())
            actionMenu.mapManager.isBoardDestination(destination, moveCommand.unit)
        }, { actionMenu ->

            val destination = actionMenu.moveCommand!!.path.route.last()
            val transport =
                actionMenu.mapManager.grid[destination.vector.x][destination.vector.y].unit

            val boardCommand = BoardCommand(actionMenu.moveCommand!!.unit, transport!!)
            boardCommand.execute()

            actionMenu.replayManager.append(
                UnitActionCommand(actionMenu.moveCommand!!, boardCommand)
            )

            // Finish this move
            actionMenu.finishMove()
        }),

        CAPTURE("Capture", { actionMenu, moveCommand ->
            val unit = moveCommand.unit
            val destination = actionMenu.mapManager.toGridRef(moveCommand.path.route.last())
            val terrain = destination.terrain

            // TODO: allies
            terrain.isProperty
                    && (terrain as Property).team != unit.team
                    && unit.type.canCapture
                    && !BOARD.isShowable(actionMenu, moveCommand)
        }, { actionMenu ->

            val destination = actionMenu.moveCommand!!.path.route.last()
            val unit = actionMenu.moveCommand!!.unit

            val property =
                actionMenu.mapManager.grid[destination.vector.x][destination.vector.y].terrain as Property

            val captureCommand = CaptureCommand(unit, property)
            captureCommand.execute()

            actionMenu.replayManager.append(
                UnitActionCommand(actionMenu.moveCommand!!, captureCommand)
            )

            // Finish this move
            actionMenu.finishMove()
        }),

        ATTACK("Attack", { actionMenu, moveCommand ->
            val destination = actionMenu.mapManager.toGridRef(moveCommand.path.route.last())

            destination.targets.size > 0
                    && !BOARD.isShowable(actionMenu, moveCommand)
        }, { actionMenu ->

            // Show the TargetingUI
            actionMenu.toTargetingState()

        }),

        UNLOAD("Unload", { _, moveCommand ->
            // TODO: If there are boarded units which can't disembark, show the button, but disabled
            //  Mobius Front '83 has similar textual feedback for unavailable actions.
            moveCommand.unit.hasUnloadableUnits()
        }, { actionMenu ->

            val actingState = actionMenu.toUnloadingState()
            actionMenu.unloadMenu.show(actionMenu.moveCommand!!.unit, actingState)

        }),

        HOLD("Hold", { actionMenu, moveCommand ->
            // TODO: For now, only transports can Hold, as a power move.
            //  Hold is actually relevant for all units, but only really for Supply and Join
            //  optimisation, so I think it's just confusing and tedious.
            !BOARD.isShowable(actionMenu, moveCommand) &&
                    moveCommand.unit.movesLeft > 0 && moveCommand.unit.type.boardable != null
        }, { actionMenu ->

            actionMenu.replayManager.append(
                actionMenu.moveCommand!!
            )
            // Finish this move
            actionMenu.finishMove()
        }),

        WAIT("Wait", { actionMenu, moveCommand ->
            !HOLD.isShowable(actionMenu, moveCommand) && !BOARD.isShowable(actionMenu, moveCommand)
        }, { actionMenu ->

            val waitCommand = WaitCommand(actionMenu.moveCommand!!.unit)
            waitCommand.execute()

            actionMenu.replayManager.append(
                UnitActionCommand(actionMenu.moveCommand!!, waitCommand)
            )

            // Finish this move
            actionMenu.finishMove()
        }),

    }

    fun show(movedState: MovedState, moveCommand: MoveCommand) {
        this.movedState = movedState
        this.moveCommand = moveCommand

        // Evaluate potential actions
        Actions.values().forEach { action ->
            val button = actionButtons[action]
            if (action.isShowable(this, moveCommand)) {
                actionTable.add(button)
                actionTable.row()
            }
        }
    }

    fun clear() {
        // Let's see if we need to null anything.
//        this.movedState = null
//        this.moveCommand = null
        actionTable.clearChildren()
    }

    private fun finishMove() {
        clear()
        mapManager.clearRanges()

        selectionStateManager.defaultState()
    }

    private fun toUnloadingState(): UnloadingState {
        val stack = movedState!!.stack
        stack.addLast(movedState!!)

        // WARNING: This will clear() ActionMenu on init!
        val unloadingState = UnloadingState(stack, unloadMenu, movedState!!, moveCommand!!)

        selectionStateManager.state = unloadingState
        return unloadingState
    }

    private fun toTargetingState() {
        val stack = movedState!!.stack
        stack.addLast(movedState!!)

        // WARNING: This will clear() ActionMenu on init!
        val targetingState = TargetingState(
            stack,
            movedState!!,
            moveCommand!!,
            replayManager,
            targetingUI
        )

        selectionStateManager.state = targetingState
        targetingUI.show()
    }
}