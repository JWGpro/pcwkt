package com.example.classic.selectionstate

import com.example.api.AStar
import com.example.classic.ServiceLocator
import com.example.classic.commands.MoveCommand
import com.example.classic.units.AUnit

class MovedState(
    val stack: ArrayDeque<SelectionState>,
    private val unit: AUnit,
    private val path: AStar.Path
) :
    SelectionState {

    private val mapManager = ServiceLocator.mapManager
    private val actionMenu = ServiceLocator.actionMenu

    private var blocking = true

    init {
        // Starts off blocking
        mapManager.clearRanges()
        val moveCommand = MoveCommand(unit, path)
        unit.move(path, {
            blocking = false

            // Defer to ActionMenu
            actionMenu.show(this, moveCommand)
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
        unit.move(path, {}, skipAnim = true, reverse = true)
        mapManager.displayRanges(unit)

        return stack.removeLast()
    }
}