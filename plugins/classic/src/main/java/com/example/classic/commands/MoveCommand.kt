package com.example.classic.commands

import com.example.classic.MapManager
import com.example.classic.units.AUnit

// While this is a self-contained Command, it should always belong to a UnitActionCommand before
//  going into a replay.
class MoveCommand(
    val unit: AUnit,
    val destination: MapManager.GridReference
) : Command {

    private val preMovePosition = unit.gridRef

    // Set by UnitActionCommand
    lateinit var after: () -> Unit

    override fun execute() {
        unit.move(destination, after)
    }

    override fun undo() {
        unit.move(preMovePosition, {})
    }
}