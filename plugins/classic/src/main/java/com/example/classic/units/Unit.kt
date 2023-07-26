package com.example.classic.units

import com.example.classic.CellVector
import com.example.classic.Team

abstract class Unit(x: Int, y: Int, team: Team) {
    //    abstract val sprite: Assets.Textures
//    private val actor = MapActor(TextureRegion(Texture(sprite.path)))
    var position = CellVector(x, y)
}