package com.example.classic.selectionstate

import com.example.classic.ReplayManager
import com.example.classic.ServiceLocator
import com.example.classic.commands.MoveCommand
import com.example.classic.commands.action.AttackCommand
import com.example.classic.commands.action.UnitActionCommand
import com.example.classic.ui.TargetingUI

class TargetingState(
    private val stack: ArrayDeque<SelectionState>,
    private val movedState: MovedState,
    private val moveCommand: MoveCommand,
    private val replayManager: ReplayManager,
    private val targetingUI: TargetingUI
) : SelectionState {

    private val unit = moveCommand.unit
    private val startingNode = moveCommand.path.route.first()

    // TODO: I have to compute indirectAllowed and then also make someone else compute it?
    //  At least we should have a common point for computing this. I just made the mistake of
    //  having to duplicate a fix.
    private val indirectAllowed = unit.gridRef!!.vector == startingNode.vector

    private val mapManager = ServiceLocator.mapManager
    private val actionMenu = ServiceLocator.actionMenu

    init {
        actionMenu.clear()

        mapManager.displayTargets(unit, mutableSetOf(unit.gridRef!!), startingNode)
    }

    override fun advance(): SelectionState {
        // Attempt to attack the target under the cursor
        val targetUnit = mapManager.getCursorNode().unit

        if (targetUnit in unit.gridRef!!.targets) {
            val weapons = unit.validWeapons(unit.gridRef!!.vector, targetUnit!!, indirectAllowed)

            val attackCommand = AttackCommand(unit, targetUnit, weapons.first())
            attackCommand.execute()

            replayManager.append(
                UnitActionCommand(moveCommand, attackCommand)
            )

            targetingUI.clear()
            mapManager.clearRanges()
            return DefaultState(mapManager)
        } else {
            return this
        }
    }

    override fun undo(): SelectionState {
        mapManager.clearRanges()

        // Show action menu again
        actionMenu.show(movedState, moveCommand)

        targetingUI.clear()

        return stack.removeLast()
    }
}