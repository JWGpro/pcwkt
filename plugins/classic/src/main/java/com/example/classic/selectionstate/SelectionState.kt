package com.example.classic.selectionstate

interface SelectionState {
    fun advance(): SelectionState
    fun undo(): SelectionState
}