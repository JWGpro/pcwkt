package com.example.classic.selectionstate

import com.example.classic.MapManager
import com.example.classic.ServiceLocator
import com.example.classic.commands.MoveCommand
import com.example.classic.units.AUnit

class MovedState(
    private val stack: ArrayDeque<SelectionState>,
    private val unit: AUnit,
    private val targetNode: MapManager.GridReference
) :
    SelectionState {

    private val mapManager = ServiceLocator.mapManager
    private val actionMenu = ServiceLocator.actionMenu

    private val startingNode = unit.gridRef
    private var blocking = true

    init {
        // Starts off blocking
        mapManager.hideRanges()
        val moveCommand = MoveCommand(unit, targetNode)
        unit.move(targetNode, {
            blocking = false

            // Defer to ActionMenu
            actionMenu.show(moveCommand)
        })
    }

    override fun advance(): SelectionState {
        // TODO: Skip each of the MoveActions. Need to pipe the SequenceAction from MapActor.
        if (blocking) return this

        // Nothing here. ActionMenu is handling this.
        return this
    }

    override fun undo(): SelectionState {
        if (blocking) return this

        actionMenu.clear()
        mapManager.showRanges()
        unit.move(startingNode, {}, skipAnim = true)

        return stack.removeLast()
    }
}