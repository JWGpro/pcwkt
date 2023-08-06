package com.example.classic

import com.example.api.HistoryList
import com.example.classic.commands.Command

class ReplayManager {

    private val replay = HistoryList<Command>()

    init {
        InputHandler.addListener(Controls.REPLAY_UNDO) { replayBackward() }
        InputHandler.addListener(Controls.REPLAY_REDO) { replayForward() }
    }

    fun append(command: Command) {
        replay.append(command)
    }

    private fun replayBackward() {
        // TODO: Make sure state is DEFAULT, and set it to REPLAY.
        //  (An old Lua comment. Not sure how to manage this with the SelectionStates.)

        val previousMove = replay.goBackward()
        if (previousMove == null) {
            println("Replay: This is the start of the game!")
        } else {
            println("Replay: Undoing command '${previousMove}'...")
            previousMove.undo()
        }
    }

    private fun replayForward() {
        val nextMove = replay.goForward()
        if (nextMove == null) {
            println("Replay: This is the end of the replay!")
        } else {
            println("Replay: Redoing command '${nextMove}'...")
            nextMove.execute()
        }
    }
}