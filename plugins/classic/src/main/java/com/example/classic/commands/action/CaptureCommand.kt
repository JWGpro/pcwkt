package com.example.classic.commands.action

import com.example.classic.commands.Command
import com.example.classic.terrains.Property
import com.example.classic.units.AUnit

class CaptureCommand(val unit: AUnit, val property: Property) : Command {

    private val originalTeam = property.team
    private val originalCaptureStrength = property.captureStrength

    override fun execute() {
        property.capture(unit)
        unit.waitHere()
    }

    override fun undo() {
        unit.unwait()
        property.switchToTeam(originalTeam)
        property.captureStrength = originalCaptureStrength
    }
}