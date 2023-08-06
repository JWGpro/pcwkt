package com.example.classic.commands.action

import com.example.classic.commands.Command
import com.example.classic.commands.MoveCommand

class UnitActionCommand(val moveCommand: MoveCommand, val actionCommand: Command) : Command {

    init {
        moveCommand.after = { actionCommand.execute() }
    }

    override fun execute() {
        moveCommand.execute()
    }

    override fun undo() {
        actionCommand.undo()
        moveCommand.undo()
    }
}