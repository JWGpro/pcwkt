package com.example.classic.units

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.example.api.AStar
import com.example.api.MapActorS
import com.example.classic.Assets
import com.example.classic.MapManager
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
    var gridRef: MapManager.GridReference
    var movesLeft = moveRange
    val boardedUnits = mutableListOf<AUnit>()
    var isOrderable = true

    init {
        // TODO: UnitFactory?
        actor.setPosition(mapManager.long(x), mapManager.long(y))
        gameStage.addActor(actor)

        gridRef = mapManager.grid[x][y]
        // TODO: store in teamUnits
    }

    fun canBoard(unit: AUnit): Boolean {
        return boardable?.contains(unit::class) == true && boardedUnits.size < boardCap
    }

    fun move(
        destination: MapManager.GridReference,
        after: () -> Unit,
        rewind: Boolean = false
    ) {
        if (destination == gridRef) {
            // No movement needed
            after()
            return
        }

        if (rewind) {
            val x = mapManager.long(destination.vector.x)
            val y = mapManager.long(destination.vector.y)
            actor.setPosition(x, y)
        } else {
            val path = AStar.findPath(gridRef, destination)
            path?.let {
                val routeSteps = it.route.map { node ->
                    val x = mapManager.long(node.vector.x)
                    val y = mapManager.long(node.vector.y)
                    MapActorS.RouteStep(x, y, 0.05f * (node.cost ?: 0))
                }
                actor.moveTo(routeSteps, after)
            }
        }

        // Only kill the ref if the coordinates the unit holds, do in fact refer to itself on the grid.
        // Otherwise, we might kill the ref of its transport.

        // TODO: Does this need to be done now?
//        if (gridRef.unit == this) gridRef.unit = null
        gridRef = destination
//        gridRef.unit = this
    }
}