package com.example.classic.commands

import com.example.api.AStar
import com.example.classic.units.AUnit

// While this is a self-contained Command, it should always belong to a UnitActionCommand before
//  going into a replay.
class MoveCommand(
    val unit: AUnit,
    val path: AStar.Path
) : Command {

    // Normally set by UnitActionCommand
    var after: () -> Unit = {}

    override fun execute() {
        unit.move(path, after)
    }

    override fun undo() {
        unit.move(path, {}, reverse = true)
    }
}