package com.example.api

class HistoryList<T> {

    private val history = mutableListOf<T>()
    private var historyPosition = 0

    fun append(element: T) {
        history.add(element)
        historyPosition++
    }

    fun removeLast() {
        history.removeLast()
        historyPosition--
    }

    fun goBackward(): T? {
        if (historyPosition == 0) return null

        val previousMove = history[historyPosition - 1]
        historyPosition--
        return previousMove
    }

    fun goForward(): T? {
        if (historyPosition == history.size) return null

        historyPosition++
        return history[historyPosition - 1]
    }

}