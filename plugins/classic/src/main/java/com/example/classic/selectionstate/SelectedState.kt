package com.example.classic.selectionstate

import com.example.api.AStar
import com.example.classic.ServiceLocator
import com.example.classic.units.AUnit

class SelectedState(
    private val stack: ArrayDeque<SelectionState>,
    private val unit: AUnit
) :
    SelectionState {
    private val mapManager = ServiceLocator.mapManager
    private val turnManager = ServiceLocator.turnManager

    init {
        mapManager.displayRanges(unit)
    }

    override fun advance(): SelectionState {
        val targetNode = mapManager.getCursorNode()

        if (unit.team == turnManager.teamsPlaying.current()
            && mapManager.isValidDestination(targetNode)
        ) {
            val path = AStar.findPath(unit.gridRef, targetNode)

            stack.addLast(this)
            return MovedState(stack, unit, path!!)
        }

        return this
    }

    override fun undo(): SelectionState {
        mapManager.clearRanges()

        return stack.removeLast()
    }
}