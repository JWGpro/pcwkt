package com.example.classic.selectionstate

import com.example.classic.ServiceLocator

class DefaultState : SelectionState {
    private val mapManager = ServiceLocator.mapManager

    // Stores states so you don't have to re-instantiate them when undoing, you just pop().
    // This also means that two states could converge on one when advancing,
    //  but still easily diverge when undoing.
    // Probably could not be managed by the SelectionStateManager, because addition is conditional.
    private val stack: ArrayDeque<SelectionState> = ArrayDeque()

    override fun advance(): SelectionState {
        val targetNode = mapManager.getCursorNode()
        val unit = targetNode.unit

        if (unit != null && unit.isOrderable) {
            stack.addLast(this)
            return SelectedState(stack, unit)
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