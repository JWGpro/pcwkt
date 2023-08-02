package com.example.classic

import com.example.api.CyclingArray
import com.example.classic.units.AUnit

class TurnManager {
    val teamUnits: Map<Team, MutableList<AUnit>> = Team.values().associateWith {
        mutableListOf()
    }
    val teamsPlaying = CyclingArray(arrayOf(Team.RED, Team.BLUE))

    init {
        InputHandler.addListener(Controls.NEXT_TURN) { cycleTurn() }
    }

    private fun cycleTurn(rewind: Boolean = false) {
        teamUnits[teamsPlaying.current()]?.forEach { unit ->
            unit.restore()
        }
        println("It's ${teamsPlaying.cycle(rewind)}'s turn${if (rewind) " again..." else "!"}")
    }
}