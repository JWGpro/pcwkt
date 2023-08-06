package com.example.classic.commands

import com.example.classic.ServiceLocator

class CycleTurn : Command {

    private val turnManager = ServiceLocator.turnManager
    private val teamsPlaying = turnManager.teamsPlaying

    // Store states of living units for the undo()
    private val thisTeamUnits =
        turnManager.teamUnits[teamsPlaying.current()]!!.filter { unit -> !unit.isDead() }

    private class AUnitTurnState(val movesLeft: Int, val isOrderable: Boolean)

    private val stateStore = thisTeamUnits.associateWith { unit ->
        AUnitTurnState(unit.movesLeft, unit.isOrderable)
    }

    override fun execute() {
        thisTeamUnits.forEach { unit ->
            unit.restore()
        }

        println("It's ${teamsPlaying.cycle()}'s turn!")
    }

    override fun undo() {
        println("It's ${teamsPlaying.cycle(backwards = true)}'s turn again...")

        stateStore.entries.forEach { (unit, state) ->
            unit.movesLeft = state.movesLeft

            if (!state.isOrderable) {
                unit.waitHere()
            }
        }
    }
}