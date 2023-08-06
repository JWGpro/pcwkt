package com.example.classic.units

import com.example.classic.Assets
import com.example.classic.MovementTypes
import com.example.classic.Team

class APC(x: Int, y: Int, team: Team) :
    AUnit(
        x,
        y,
        team,
        Assets.Textures.APC_RED,
        "APC",
        4000,
        6,
        MovementTypes.TRACK,
        boardCap = 1,
        boardable = arrayOf(Infantry::class)
    ) {
}