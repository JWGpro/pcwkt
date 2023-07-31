package com.example.classic.selectionstate

import com.example.classic.MapManager
import com.example.classic.ServiceLocator
import com.example.classic.units.AUnit

class MovedState(private val unit: AUnit, private val node: MapManager.GridReference) :
    SelectionState {
    private val mapManager = ServiceLocator.mapManager

    init {
        mapManager.hideRanges()

//        indirectallowed = (selunit.pos:equals(destination))
//        moveCommand = com.MoveCommand(selunit, destination)
//        state = STATES.BLOCKING

//        q:queue(function() -- The statements following the MoveCommand must be queued to occur after its finish.
//        pri.evaluateActions()
//        state = STATES.MOVED

        // TODO: Implement Command first

    }

    override fun advance(): SelectionState {
        println("Some action from here, $unit / $node")
        return this
    }

    override fun undo(): SelectionState {
        // TODO: Undo the move
        mapManager.showRanges()
        return SelectedState(unit)
    }
}