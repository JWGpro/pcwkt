package com.example.classic.units

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.example.api.CellVector
import com.example.api.MapActorS
import com.example.classic.Assets
import com.example.classic.MoveTypes
import com.example.classic.ServiceLocator
import com.example.classic.Team
import kotlin.reflect.KClass

// "Unit" was too close to the Kotlin inbuilt
abstract class AUnit(
    x: Int,
    y: Int,
    var team: Team,
    private val sprite: Assets.Textures,
    val name: String,
    val price: Int,
    val moveRange: Int,
    val moveType: MoveTypes,
    val boardCap: Int = 0,
    // TODO: Can't figure out the syntax for "subclass of AUnit".
    val boardable: Array<KClass<*>>? = null
) {
    private val assetManager = ServiceLocator.assetManager
    private val mapManager = ServiceLocator.mapManager
    private val gameStage = ServiceLocator.gameStage

    private val actor =
        MapActorS(TextureRegion(assetManager.get<Texture>(sprite.path)))
    private val maxHp = 100f

    var hp = maxHp
    var vector: CellVector
    var movesLeft = moveRange

    init {
        // TODO: UnitFactory?
        actor.setPosition(mapManager.long(x), mapManager.long(y))
        gameStage.addActor(actor)

        vector = mapManager.grid[x][y].vector
        // store in teamUnits
    }
}