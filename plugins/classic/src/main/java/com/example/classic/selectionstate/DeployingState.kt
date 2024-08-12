package com.example.classic.selectionstate

import com.example.classic.ServiceLocator
import com.example.classic.terrains.Property

class DeployingState(
    private val stack: ArrayDeque<SelectionState>,
    private val property: Property
) : SelectionState {

    private val deployMenu = ServiceLocator.deployMenu

    init {
        deployMenu.show(this, property)
    }

    override fun advance(): SelectionState {
        // A menu is handling this
        return this
    }

    override fun undo(): SelectionState {
        deployMenu.clear()
        return stack.removeLast()
    }
}