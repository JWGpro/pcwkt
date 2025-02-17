package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.scenes.scene2d.Stage
import com.example.api.AStar
import com.example.api.CellVector
import com.example.api.Util.clampMax
import com.example.api.Util.clampMin
import com.example.classic.serial.GridRefSerial
import com.example.classic.serial.TerrainSerial
import com.example.classic.terrains.Property
import com.example.classic.terrains.Terrain
import com.example.classic.terrains.TerrainType
import com.example.classic.terrains.TilePath
import com.example.classic.units.AUnit
import com.example.classic.units.AUnitFactory
import com.example.classic.units.AUnitType
import kotlin.math.abs
import kotlin.math.floor

private const val EXT = "pcwkt"
private const val CELL_SIZE = 16

// TODO: May need to expand this to allow for more intricate rendering.
//  e.g. Mobius Front '83; looks more textured than tiled.
//  This has nothing to do with multiplayer business logic, so game versions with just updated
//  rendering code could still play online with older clients running the same gamemode.
//  i.e. Spring's serialVersionUID.
class MapManager(
    assetManager: AssetManager,
    gameStage: Stage,
    private val tiledMap: TiledMap,
    private val cursor: Cursor,
    private val turnManager: TurnManager,
    private val serialGrid: List<List<GridRefSerial>>
) {
    class GridReference(
        vector: CellVector,
        neighbours: MutableSet<AStar.Node>,
        var unit: AUnit?,
        var terrain: Terrain,
        var targets: MutableSet<AUnit>
    ) : AStar.Node(vector, neighbours, null)

    private val mapW = serialGrid.size
    private val mapH = serialGrid[0].size
    val grid = Array(mapW) {
        Array(mapH) {
            GridReference(
                CellVector(0, 0),
                mutableSetOf(),
                null,
                TerrainType.SEA,
                mutableSetOf()
            )
        }
    }
    private val terrainLayer = newMapLayer("terrain")
    private val terrainSet = newTileSet("terrain")
    private val moveRangeLayer = newMapLayer("moveRange")
    private val attackRangeLayer = newMapLayer("attackRange")
    private val rangesSet = newTileSet("ranges")

    private val unitFactory = AUnitFactory(assetManager, gameStage, turnManager, this)

    init {
        // Generate tilesets
        TilePath.values().forEach { terrainPath ->
            val tile = newStaticTile("$EXT/terrain-assets/default/${terrainPath.path}")
            terrainSet.putTile(terrainPath.ordinal, tile)
        }
        RangeTiles.values().forEach { rangeTile ->
            val tile = newStaticTile("$EXT/ui-assets/default/${rangeTile.path}")
            rangesSet.putTile(rangeTile.ordinal, tile)
        }

        // Init grid and terrain
        forMap { x, y ->
            grid[x][y].vector = CellVector(x, y)
            setTerrain(x, y, serialGrid[x][y].terrain)

            serialGrid[x][y].unit?.let { unit ->
                unitFactory.make(x, y, unit.team, unit.type)
            }

            // I don't know why it doesn't pick up the MutableSet that I declared.
            val neighbours = grid[x][y].neighbours as MutableSet<AStar.Node>
            neighbours.add(grid[x][clampMax(y + 1, mapH - 1)])
            neighbours.add(grid[x][clampMin(y - 1, 0)])
            neighbours.add(grid[clampMin(x - 1, 0)][y])
            neighbours.add(grid[clampMax(x + 1, mapW - 1)][y])
        }

    }

    fun makeUnit(vector: CellVector, team: Team, unitType: AUnitType) {
        unitFactory.make(vector.x, vector.y, team, unitType)
    }

    fun updateCursor(x: Float, y: Float) {
        // TODO:
        //  Works with: panning, zooming.
        //  Doesn't work with: resizing.
        cursor.setPosition(snap(x), snap(y))
    }

    private fun snap(coordinate: Float): Float {
        // Snaps a "long" coordinate onto the grid.
        // Returns a "long" coordinate.
        return CELL_SIZE * floor(coordinate / CELL_SIZE)
    }

    fun long(coordinate: Int): Float {
        // Upscales a "short" grid coordinate into a "long" coordinate.
        //  e.g. for placing actors in the stage.
        return (CELL_SIZE * coordinate).toFloat()
    }

    fun short(coordinate: Float): Int {
        // Downscales a "long" coordinate to a "short" grid coordinate.
        //  e.g. for human-readable display, checking the grid.
        return floor(coordinate / CELL_SIZE).toInt()
    }

    private fun newMapLayer(name: String): TiledMapTileLayer {
        val layers = tiledMap.layers
        val tileLayer = TiledMapTileLayer(mapW, mapH, CELL_SIZE, CELL_SIZE)
        tileLayer.name = name
        layers.add(tileLayer)

        forMap { x, y ->
            // Fills the layer with cells - tiles should then be assigned to the cells.
            val cell = TiledMapTileLayer.Cell()
            tileLayer.setCell(x, y, cell)
        }
        return tileLayer
    }

    private fun newTileSet(name: String): TiledMapTileSet {
        val tileSet = TiledMapTileSet()
        tileSet.name = name
        tiledMap.tileSets.addTileSet(tileSet)
        return tileSet
    }

    private fun newStaticTile(path: String): StaticTiledMapTile {
        val fh = ExternalFileHandleResolver().resolve(path)
        return StaticTiledMapTile(TextureRegion(Texture(fh)))
    }

    private fun setTerrain(x: Int, y: Int, terrain: TerrainSerial) {

        if (terrain.type.isProperty) {
            grid[x][y].terrain = Property(grid[x][y], terrain.type, terrain.team!!, this)

            terrainLayer.getCell(x, y).tile =
                terrainSet.getTile(terrain.type.paths[terrain.team]!!.ordinal)
        } else {
            grid[x][y].terrain = terrain.type

            terrainLayer.getCell(x, y).tile =
                terrainSet.getTile(terrain.type.paths[Team.NEUTRAL]!!.ordinal)
        }

    }

    fun changePropertyTeam(property: Property, team: Team) {
        val gridRef = property.gridRef

        terrainLayer.getCell(gridRef.vector.x, gridRef.vector.y).tile =
            terrainSet.getTile(property.type.paths[team]!!.ordinal)
    }

    private fun manRange(start: CellVector, minRange: Int, maxRange: Int): Collection<CellVector> {
        // Returns the Manhattan or Taxicab "circle" range from a starting point, clamping within
        //  the map w/h, taking into account a minimum range. Assumes the origin is always 0,0.
        // Used to get ranges, e.g. for movement and attack.

        val vectors = mutableSetOf<CellVector>()

        // Sets the initial x bounds to between +/- maxrange (clamped within the map).
        val minX = clampMin(start.x - maxRange, 0)
        val maxX = clampMax(start.x + maxRange, mapW - 1)
        for (x in minX until maxX + 1) {
            val dx = abs(start.x - x)
            val yRange = maxRange - dx
            // Sets the y bounds to whatever is left of the range after traversing x (again clamped
            //  within the map).
            val minY = clampMin(start.y - yRange, 0)
            val maxY = clampMax(start.y + yRange, mapH - 1)
            for (y in minY until maxY + 1) {
                val dy = abs(start.y - y)
                // Proceed if Manhattan distance >= minrange.
                if (dx + dy >= minRange) {
                    vectors.add(grid[x][y].vector)
                }
            }
        }
        return vectors
    }

    private fun setCosts(unit: AUnit) {
        forMap { x, y ->
            val occupier = grid[x][y].unit

            // TODO: Check for allies
            // An enemy will prevent passage.
            if (occupier != null && occupier.team != unit.team) {
                grid[x][y].cost = null
            } else {
                grid[x][y].cost = grid[x][y].terrain.moveCosts[unit.type.movementType]
            }
        }
    }

    fun displayRanges(unit: AUnit) {
        val destinationNodes = mutableSetOf<GridReference>()

        val moveVectors = manRange(unit.gridRef!!.vector, 0, unit.movesLeft)

        moveVectors.forEach { vec ->
            setCosts(unit)
            val path = AStar.findPath(unit.gridRef!!, grid[vec.x][vec.y])
            // TODO: Incorporate movesLeft into AStar.findPath().
            if (path != null && path.totalCost <= unit.movesLeft) {
                val destination = grid[vec.x][vec.y]
                val cell = moveRangeLayer.getCell(vec.x, vec.y)
                // Destination conditions:
                // 1: Cell is empty, or occupied by this unit.
                if (isMoveDestination(destination, unit)) {
                    cell.tile = rangesSet.getTile(RangeTiles.MOVE.ordinal)
                    destinationNodes.add(destination)
                } else if (destination.unit?.team == unit.team) { // TODO: or allies
                    // 2: Cell is occupied by a boardable unit.
                    if (isBoardDestination(destination, unit)) {
                        cell.tile = rangesSet.getTile(RangeTiles.BOARD.ordinal)
                    }
                    // 3: Allow ONLY PASSAGE for units of the same or allied teams.
                    else {
                        cell.tile = rangesSet.getTile(RangeTiles.SELECT.ordinal)
                    }

                }
            }
        }

        displayTargets(unit, destinationNodes, unit.gridRef!!)
    }

    fun displayTargets(
        unit: AUnit,
        destinationNodes: MutableSet<GridReference>,
        startingNode: AStar.Node
    ) {
        unit.type.getWeapons()?.forEach { weapon ->
            destinationNodes.forEach { destination ->
                val indirectAllowed = startingNode.vector == destination.vector

                if (weapon.isDirect || indirectAllowed) {
                    val targets = mutableSetOf<AUnit>()
                    destination.targets = targets

                    val attackVectors =
                        manRange(destination.vector, weapon.minRange, weapon.maxRange)
                    attackVectors.forEach { vector ->
                        val target = grid[vector.x][vector.y].unit

                        if (target != null
                            && target.team != unit.team  // TODO: or ally
                            && weapon.damageMap.containsKey(target.type)
                        ) {
                            targets.add(target)

                            val cell = attackRangeLayer.getCell(vector.x, vector.y)
                            cell.tile = rangesSet.getTile(RangeTiles.ATTACK.ordinal)
                        }
                    }
                }
            }
        }
    }

    fun getCursorNode(): GridReference {
        val cursorX = short(cursor.actor.x)
        val cursorY = short(cursor.actor.y)

        // TODO 2024-08-10: I wrote this a year ago and now I don't know why. Let's go with it for now
        if (cursorX < 0 || cursorX > mapW - 1 || cursorY < 0 || cursorY > mapH - 1) return grid[0][0]

        return grid[cursorX][cursorY]
    }

    fun clearRanges() {
        forMap { x, y ->
            val moveCell = moveRangeLayer.getCell(x, y)
            moveCell.tile = null
            val attackCell = attackRangeLayer.getCell(x, y)
            attackCell.tile = null
        }
    }

    private fun isMoveDestination(destination: GridReference, unit: AUnit): Boolean {
        return destination.unit == null || destination.unit == unit
    }

    fun isBoardDestination(destination: GridReference, unit: AUnit): Boolean {
        return destination.unit?.canBoard(unit) == true
    }

    fun isValidDestination(destination: GridReference, unit: AUnit): Boolean {
        return isMoveDestination(destination, unit) || isBoardDestination(destination, unit)
    }

    fun toGridRef(node: AStar.Node): GridReference {
        // grid is public so this is just convenience
        return grid[node.vector.x][node.vector.y]
    }

    private inline fun forMap(fn: (x: Int, y: Int) -> Unit) {
        for (x in 0 until mapW) {
            for (y in 0 until mapH) {
                fn(x, y)
            }
        }
    }

}