package com.example.classic.units

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.example.api.AStar
import com.example.api.MapActorS
import com.example.classic.Assets
import com.example.classic.MapManager
import com.example.classic.MovementTypes
import com.example.classic.ServiceLocator
import com.example.classic.Team
import kotlin.reflect.KClass

// "Unit" was too close to the Kotlin inbuilt
// TODO: AUnit subclasses should be implemented in data. It would be an obvious, and probably by far
//  the most common, use case that people would want to tweak the numbers on existing units so as
//  to make a custom ruleset - not having to recompile the game into a separate mode to do this.
//  ...Though this opens up the question of how much else ought to be data-driven.
//  Client-side customisation of assets (and localisation strings) is yet another issue.
abstract class AUnit(
    x: Int,
    y: Int,
    var team: Team,
    private val sprite: Assets.Textures,
    val name: String,
    val price: Int,
    val moveRange: Int,
    val moveType: MovementTypes,
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
        // MapManager.placeUnit() will set `gridRef.unit = this`
    }

    fun canBoard(unit: AUnit): Boolean {
        return boardable?.contains(unit::class) == true && boardedUnits.size < boardCap
    }

    fun move(
        destination: MapManager.GridReference,
        after: () -> Unit,
        skipAnim: Boolean = false
    ) {
        if (destination == gridRef) {
            // No movement needed
            after()
            return
        }

        if (skipAnim) {
            val x = mapManager.long(destination.vector.x)
            val y = mapManager.long(destination.vector.y)
            actor.setPosition(x, y)
            after()
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

        killUnitRef()
        gridRef = destination
        storeUnitRef()
    }

    // Got some "accidental override" error when this was called "wait".
    fun waitHere() {
        isOrderable = false
        actor.color = Color.GRAY
    }

    fun unwait() {
        isOrderable = true
        actor.color = Color.WHITE
    }

    fun restore() {
        movesLeft = moveRange
        unwait()
    }

    fun isDead(): Boolean {
        return hp <= 0f
    }

    private fun killUnitRef() {
        // Only wipe the unit ref if it did in fact refer to this unit. That is, a normal move.
        // This will not be the case if this unit is moving out of a transport.
        if (gridRef.unit == this) gridRef.unit = null
    }

    private fun storeUnitRef() {
        // Similarly, don't set the unit ref if this unit is moving onto a transport.
        if (gridRef.unit == null) gridRef.unit = this
    }
}