package com.example.classic.selectionstate

import com.example.classic.ServiceLocator
import com.example.classic.commands.MoveCommand
import com.example.classic.ui.UnloadMenu

class ActingState(
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
        // Attack/Unload/whatever menu is handling this
        return this
    }

    override fun undo(): SelectionState {
        // Show action menu again
        actionMenu.show(movedState, moveCommand)

        // TODO: And targetMenu
        unloadMenu.clear()

        return stack.removeLast()
    }
}