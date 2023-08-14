package com.example.classic.commands.action

import com.example.classic.commands.Command
import com.example.classic.units.AUnit

class BoardCommand(val cargo: AUnit, val transport: AUnit) : Command {
    override fun execute() {
        cargo.board(transport)
    }

    override fun undo() {
        cargo.disembark()
    }
}