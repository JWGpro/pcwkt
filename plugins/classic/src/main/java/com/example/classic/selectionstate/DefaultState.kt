package com.example.classic.selectionstate

import com.example.classic.ServiceLocator

// TODO: Should be static
//  Successive states could be stored in a stack or something. Then you undo by popping the stack.
class DefaultState : SelectionState {
    private val mapManager = ServiceLocator.mapManager

    override fun advance(): SelectionState {
        val targetNode = mapManager.getCursorNode()
        val unit = targetNode.unit

        if (unit != null && unit.isOrderable) {
            return SelectedState(unit)
        }
        // TODO: else select terrain

        // Nothing to select
        return this
    }

    override fun undo(): SelectionState {
        // Do nothing. Maybe play an "error" sound.
        return this
    }
}