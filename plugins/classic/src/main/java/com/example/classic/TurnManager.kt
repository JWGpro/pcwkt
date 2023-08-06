package com.example.classic

import com.example.api.CyclingArray
import com.example.classic.commands.CycleTurn
import com.example.classic.units.AUnit

class TurnManager(private val replayManager: ReplayManager) {
    val teamUnits: Map<Team, MutableList<AUnit>> = Team.values().associateWith {
        mutableListOf()
    }
    val teamsPlaying = CyclingArray(arrayOf(Team.RED, Team.BLUE))

    init {
        InputHandler.addListener(Controls.NEXT_TURN) { nextTurn() }
    }

    private fun nextTurn() {
        val cycleTurn = CycleTurn()
        cycleTurn.execute()
        replayManager.append(cycleTurn)
    }
}