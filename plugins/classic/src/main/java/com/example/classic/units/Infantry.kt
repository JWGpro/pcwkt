package com.example.classic.units

import com.example.classic.Assets
import com.example.classic.MoveTypes
import com.example.classic.Team

class Infantry(x: Int, y: Int, team: Team) :
    AUnit(
        x,
        y,
        team,
        Assets.Textures.INF_RED_1,
        "Infantry",
        1000,
        3,
        MoveTypes.INF
    ) {
}