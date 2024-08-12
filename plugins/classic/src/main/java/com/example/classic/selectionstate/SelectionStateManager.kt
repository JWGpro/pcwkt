package com.example.classic.selectionstate

import com.example.classic.Controls
import com.example.classic.InputHandler
import com.example.classic.MapManager

class SelectionStateManager(private val mapManager: MapManager) {
    var state: SelectionState? = null

    init {
        InputHandler.addListener(Controls.SELECT_NEXT) { state = state?.advance() }
        InputHandler.addListener(Controls.CANCEL_LAST) { state = state?.undo() }
    }

    fun defaultState() {
        // This should free whatever state was here, including any stack it was holding.
        // Make sure that no SelectionStates get held by other objects!
        state = DefaultState(mapManager)
    }
}