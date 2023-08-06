package com.example.classic.commands.action

import com.example.classic.commands.Command
import com.example.classic.units.AUnit

class WaitCommand(val unit: AUnit) : Command {

    override fun execute() {
        unit.waitHere()
    }

    override fun undo() {
        unit.unwait()
    }
}