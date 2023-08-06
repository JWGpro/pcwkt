package com.example.classic.commands

interface Command {
    fun execute()
    fun undo()
}