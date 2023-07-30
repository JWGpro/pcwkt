package com.example.classic.selectionstate

import com.example.classic.Controls
import com.example.classic.InputHandler

class SelectionStateManager {
    private var state: SelectionState = DefaultState()

    init {
        InputHandler.addListener(Controls.SELECT_NEXT) { state = state.advance() }
        InputHandler.addListener(Controls.CANCEL_LAST) { state = state.undo() }
    }
}