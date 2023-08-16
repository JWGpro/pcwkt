package com.example.classic.units

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.example.api.AStar
import com.example.api.MapActorS
import com.example.classic.MapManager
import com.example.classic.Team
import kotlin.math.ceil

// "Unit" was too close to the Kotlin inbuilt
// TODO: AUnit subclasses should be implemented in data. It would be an obvious, and probably by far
//  the most common, use case that people would want to tweak the numbers on existing units so as
//  to make a custom ruleset - not having to recompile the game into a separate mode to do this.
//  ...Though this opens up the question of how much else ought to be data-driven.
//  Client-side customisation of assets (and localisation strings) is yet another issue.
class AUnit(
    x: Int,
    y: Int,
    var team: Team,
    val type: AUnitType,
    assetManager: AssetManager,
    gameStage: Stage,
    private val mapManager: MapManager,
) {
    private val spritePath = type.spritePathMap[team]!!
    val price = type.price
    private val moveRange = type.moveRange
    val moveType = type.movementType
    private val boardCap = type.boardCap
    private val boardable = type.boardable

    private val actor =
        MapActorS(gameStage, TextureRegion(assetManager.get<Texture>(spritePath)))
    private val maxHp = 100f

    var hp = maxHp
        private set
    var gridRef: MapManager.GridReference?
        private set
    var movesLeft = moveRange
    val boardedUnits = mutableListOf<AUnit>()
    var isOrderable = true
        private set
    var transport: AUnit? = null
        private set

    init {
        actor.setPosition(mapManager.long(x), mapManager.long(y))
        gameStage.addActor(actor)

        gridRef = mapManager.grid[x][y]
        gridRef!!.unit = this
    }

    fun canBoard(unit: AUnit): Boolean {
        return boardable?.contains(unit.type) == true && boardedUnits.size < boardCap
    }

    fun hasUnloadableUnits(): Boolean {
        boardedUnits.forEach { unit ->
            // If the unit can move, and can disembark where it is
            if (unit.movesLeft > 0 && this.gridRef!!.terrain.moveCosts[unit.moveType] != null) {
                return true
            }
        }
        return false
    }

    fun board(transport: AUnit) {
        gridRef = null
        this.transport = transport
        actor.hide()

        transport.boardedUnits.add(this)
    }

    fun disembark() {
        transport!!.boardedUnits.removeLast()

        gridRef = transport!!.gridRef
        transport = null

        val x = mapManager.long(gridRef!!.vector.x)
        val y = mapManager.long(gridRef!!.vector.y)
        actor.setPosition(x, y)
        actor.unhide()
    }

    fun getStrength(): Int {
        // Round up HP to ints from 1-10 for display or strength calculations (attack/capture...).
        return ceil((hp / maxHp) * 10).toInt()
    }

    fun move(
        path: AStar.Path,
        after: () -> Unit,
        skipAnim: Boolean = false,
        reverse: Boolean = false
    ) {
        val destination = if (reverse) path.route.first() else path.route.last()

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
            val routePreMap = if (reverse) path.route.dropLast(1).reversed() else path.route.drop(1)

            val routeSteps = routePreMap.map { node ->
                val x = mapManager.long(node.vector.x)
                val y = mapManager.long(node.vector.y)
                MapActorS.RouteStep(x, y, 0.05f * (node.cost ?: 0))
            }

            actor.moveTo(routeSteps, after)
        }

        movesLeft -= if (reverse) -path.totalCost else path.totalCost

        killUnitRef()
        gridRef = mapManager.grid[destination.vector.x][destination.vector.y]
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
        if (gridRef!!.unit == this) gridRef!!.unit = null
    }

    private fun storeUnitRef() {
        // Similarly, don't set the unit ref if this unit is moving onto a transport.
        if (gridRef!!.unit == null) gridRef!!.unit = this
    }
}