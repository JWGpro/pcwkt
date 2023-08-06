package com.example.classic.units

import com.example.classic.Assets
import com.example.classic.MovementTypes
import com.example.classic.Team

class Infantry(x: Int, y: Int, team: Team) :
    AUnit(
        x,
        y,
        team,
        Assets.Textures.INFANTRY.paths[team]!!,
        "Infantry",
        1000,
        3,
        MovementTypes.INF
    ) {
}