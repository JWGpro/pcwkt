package com.example.classic.commands.action

import com.example.classic.commands.Command
import com.example.classic.units.AUnit

class UnloadCommand(val cargo: AUnit) : Command {

    val transport = cargo.transport
    // TODO: Lua had code to remember where in the transport the cargo was.
    //  Not gonna implement it until there are multiple-slot transports.

    override fun execute() {
        cargo.disembark()
        // The caller has to select this unit now
    }

    override fun undo() {
        cargo.board(transport!!)
    }
}