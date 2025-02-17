package com.example.classic

import com.badlogic.gdx.Input

const val SCROLL_UP = -1
const val SCROLL_DOWN = 1

object InputHandler {

    private val listeners: Map<Controls, MutableList<() -> Unit>> =
        Controls.values().associateWith {
            mutableListOf()
        }

    // TODO: How do we make this user-overridable?
    enum class Binds(val map: Map<Int, Controls>) {
        TOUCH_UP(
            mapOf(
                Input.Buttons.LEFT to Controls.SELECT_NEXT,
                Input.Buttons.RIGHT to Controls.CANCEL_LAST
            )
        ),
        SCROLLED(
            mapOf(
                SCROLL_UP to Controls.ZOOM_IN,
                SCROLL_DOWN to Controls.ZOOM_OUT
            )
        ),
        KEY_DOWN(
            mapOf(
                Input.Keys.BACK to Controls.CANCEL_LAST, // TODO: Mobile only?
                Input.Keys.E to Controls.NEXT_TURN,
                Input.Keys.MINUS to Controls.REPLAY_UNDO,
                Input.Keys.EQUALS to Controls.REPLAY_REDO,
            )
        )
    }

    fun tryBind(inputType: Binds, inputId: Int) {
        // Looks for a bound control, and calls listeners.
        val boundControl = inputType.map[inputId]
        boundControl?.let {
            callListeners(boundControl)
        }
    }

    private fun callListeners(control: Controls) {
        listeners[control]?.forEach { listener ->
            listener()
        }
    }

    fun addListener(control: Controls, listener: () -> Unit) {
        listeners[control]?.add(listener)
    }
}