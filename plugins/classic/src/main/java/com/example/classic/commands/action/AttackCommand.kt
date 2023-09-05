package com.example.classic.commands.action

import com.example.classic.commands.Command
import com.example.classic.units.AUnit
import com.example.classic.units.WeaponType

class AttackCommand(val attacker: AUnit, val defender: AUnit, val weapon: WeaponType) : Command {

    private val attackerHp = attacker.hp
    private val defenderHp = defender.hp

    override fun execute() {
        attacker.battle(defender, weapon)
        attacker.waitHere()
    }

    override fun undo() {
        attacker.unwait()

        attacker.setHp(attackerHp)
        defender.setHp(defenderHp)
    }
}