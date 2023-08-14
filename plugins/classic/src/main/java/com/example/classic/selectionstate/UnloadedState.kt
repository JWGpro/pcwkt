package com.example.classic.selectionstate

import com.example.classic.ReplayManager
import com.example.classic.ServiceLocator
import com.example.classic.commands.action.UnloadCommand
import com.example.classic.units.AUnit

class UnloadedState(
    private val stack: ArrayDeque<SelectionState>,
    private val cargo: AUnit,
    private val replayManager: ReplayManager,
    // UnloadCommand should have been executed; it needs to happen before the SelectedState init
    private val unloadCommand: UnloadCommand,
    private val actingState: ActingState
) : SelectedState(stack, cargo) {

    private val unloadMenu = ServiceLocator.unloadMenu

    init {
        replayManager.append(
            unloadCommand
        )

        unloadMenu.clear()
    }

    // advance() unchanged from SelectedState

    override fun undo(): SelectionState {
        unloadCommand.undo()
        replayManager.removeLast()
        unloadMenu.show(cargo.transport!!, actingState)

        return super.undo()
    }
}