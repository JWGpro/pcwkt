package com.example.classic.selectionstate

import com.example.classic.ServiceLocator
import com.example.classic.commands.MoveCommand
import com.example.classic.ui.UnloadMenu

class UnloadingState(
    val stack: ArrayDeque<SelectionState>,
    private val unloadMenu: UnloadMenu,
    private val movedState: MovedState,
    private val moveCommand: MoveCommand
) : SelectionState {

    private val actionMenu = ServiceLocator.actionMenu

    init {
        actionMenu.clear()
    }

    override fun advance(): SelectionState {
        // UnloadMenu is handling this
        return this
    }

    override fun undo(): SelectionState {
        // Show action menu again
        actionMenu.show(movedState, moveCommand)

        unloadMenu.clear()

        return stack.removeLast()
    }
}