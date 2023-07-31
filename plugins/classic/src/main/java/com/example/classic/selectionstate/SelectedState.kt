package com.example.classic.selectionstate

import com.example.classic.ServiceLocator
import com.example.classic.units.AUnit

class SelectedState(private val unit: AUnit) : SelectionState {
    private val mapManager = ServiceLocator.mapManager

    init {
        mapManager.displayRanges(unit)
    }

    override fun advance(): SelectionState {
        val targetNode = mapManager.getCursorNode()

        // TODO: if unit.team == player.team
        if (mapManager.isValidDestination(targetNode)) {
            return MovedState(unit, targetNode)
        }

        return this
    }

    override fun undo(): SelectionState {
        mapManager.clearRanges()
        return DefaultState()
    }
}