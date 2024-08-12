package com.example.classic.selectionstate

import com.example.classic.MapManager
import com.example.classic.ServiceLocator
import com.example.classic.terrains.Property

// TODO 2024-08-10: Why are we still passing around the mapManager if other states use ServiceLocator?
class DefaultState(private val mapManager: MapManager) : SelectionState {

    // Stores states so you don't have to re-instantiate them when undoing, you just pop().
    // This also means that two states could converge on one when advancing,
    //  but still easily diverge when undoing.
    // Probably could not be managed by the SelectionStateManager, because addition is conditional.
    private val stack: ArrayDeque<SelectionState> = ArrayDeque()
    private val turnManager = ServiceLocator.turnManager

    override fun advance(): SelectionState {
        val targetNode = mapManager.getCursorNode()
        val unit = targetNode.unit
        val terrain = targetNode.terrain

        if (unit != null && unit.isOrderable) {
            stack.addLast(this)
            return SelectedState(stack, unit)
        }

        if (terrain.isProperty
            && (terrain as Property).team == turnManager.teamsPlaying.current()
            && terrain.unitsDeployable != null
        ) {
            stack.addLast(this)
            return DeployingState(stack, terrain)
        }

        // Nothing to select
        return this
    }

    override fun undo(): SelectionState {
        // Do nothing. Maybe play an "error" sound.
        return this
    }
}