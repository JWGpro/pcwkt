package com.example.classic

import com.badlogic.gdx.Input

const val SCROLL_UP = -1
const val SCROLL_DOWN = 1

object InputHandler {

    interface ControlObserver {
        fun receive(control: Controls)
    }

    private val observers: Map<Controls, MutableList<ControlObserver>> =
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
        )
    }

    fun tryBind(inputType: Binds, inputId: Int) {
        // Looks for a bound control, and notifies observers.
        val boundControl = inputType.map[inputId]
        boundControl?.run {
            notifyObservers(boundControl)
        }
    }

    private fun notifyObservers(control: Controls) {
        observers[control]?.forEach { observer ->
            observer.receive(control)
        }
    }

    fun registerObserver(controlObserver: ControlObserver, control: Controls) {
        observers[control]?.add(controlObserver)
    }
}