package com.example.classic.selectionstate

import com.example.classic.MapManager
import com.example.classic.ServiceLocator
import com.example.classic.units.AUnit

class MovedState(
    private val stack: ArrayDeque<SelectionState>,
    private val unit: AUnit,
    private val targetNode: MapManager.GridReference
) :
    SelectionState {

    private val mapManager = ServiceLocator.mapManager
    private val startingNode = unit.gridRef
    private var blocking = true

    init {
        // Starts off blocking
        mapManager.hideRanges()
        unit.move(targetNode, {
            blocking = false
            // TODO: evaluateActions()
            println("evaluateActions")
        })

        // TODO: Command was used here for replays
    }

    override fun advance(): SelectionState {
        // TODO: Skip each of the MoveActions. Need to pipe the SequenceAction from MapActor.
        if (blocking) return this

        // TODO: stack.addLast(this)
        println("Initiated some action")
        return this
    }

    override fun undo(): SelectionState {
        if (blocking) return this

        // TODO: actionMenu.clear()
        mapManager.showRanges()
        unit.move(startingNode, {}, rewind = true)

        return stack.removeLast()
    }
}