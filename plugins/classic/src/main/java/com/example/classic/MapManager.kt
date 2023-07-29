package com.example.classic

import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.example.api.AStar
import com.example.api.CellVector
import com.example.api.Util.clampMax
import com.example.api.Util.clampMin
import com.example.classic.units.AUnit
import com.example.classic.units.Infantry
import kotlin.math.abs
import kotlin.math.floor

private const val EXT = "pcwkt"
private const val CELL_SIZE = 16


class MapManager(
    private val tiledMap: TiledMap,
    private val cursor: Cursor
) {
    class GridReference(
        vector: CellVector,
        neighbours: MutableSet<AStar.Node>,
        var unit: AUnit?,
        var terrain: Terrains
    ) : AStar.Node(vector, neighbours, null)

    // TODO: These should come from the loaded map
    private val mapW = 30
    private val mapH = 20
    val grid = Array(mapW) {
        Array(mapH) {
            GridReference(
                CellVector(0, 0),
                mutableSetOf(),
                null,
                Terrains.SEA
            )
        }
    }
    private val terrainLayer = newMapLayer("terrain", mapW, mapH, CELL_SIZE)
    private val terrainSet = newTileSet("terrain")
    private val moveRangeLayer = newMapLayer("moveRange", mapW, mapH, CELL_SIZE)
    private val attackRangeLayer = newMapLayer("attackRange", mapW, mapH, CELL_SIZE)
    private val rangesSet = newTileSet("ranges")

    init {
        // Generate tilesets
        Terrains.values().forEach { terrain ->
            val tile = newStaticTile("$EXT/terrain-assets/default/${terrain.path}")
            terrainSet.putTile(terrain.ordinal, tile)
        }
        RangeTiles.values().forEach { rangeTile ->
            val tile = newStaticTile("$EXT/ui-assets/default/${rangeTile.path}")
            rangesSet.putTile(rangeTile.ordinal, tile)
        }

        // Init grid and terrain
        for (x in 0 until mapW) {
            for (y in 0 until mapH) {
                grid[x][y].vector = CellVector(x, y)
                setTerrain(x, y, Terrains.SEA)

                // I don't know why it doesn't pick up the MutableSet that I declared.
                val neighbours = grid[x][y].neighbours as MutableSet<AStar.Node>
                neighbours.add(grid[x][clampMax(y + 1, mapH - 1)])
                neighbours.add(grid[x][clampMin(y - 1, 0)])
                neighbours.add(grid[clampMin(x - 1, 0)][y])
                neighbours.add(grid[clampMax(x + 1, mapW - 1)][y])
            }
        }
    }

    fun loadMap() {
        // TODO: Until we get all this from a map file
        setTerrain(10, 5, Terrains.PLAIN)
        setTerrain(11, 5, Terrains.PLAIN)
        setTerrain(12, 5, Terrains.PLAIN)
        setTerrain(13, 5, Terrains.PLAIN)
        setTerrain(10, 6, Terrains.PLAIN)
        setTerrain(11, 6, Terrains.MOUNTAIN)
        setTerrain(12, 6, Terrains.MOUNTAIN)
        setTerrain(13, 6, Terrains.MOUNTAIN)
        setTerrain(10, 7, Terrains.PLAIN)
        setTerrain(11, 7, Terrains.PLAIN)
        setTerrain(12, 7, Terrains.PLAIN)
        setTerrain(13, 7, Terrains.PLAIN)

        placeUnit(Infantry(13, 7, Team.RED))
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

    private fun newMapLayer(name: String, w: Int, h: Int, cellSize: Int): TiledMapTileLayer {
        val layers = tiledMap.layers
        val tileLayer = TiledMapTileLayer(w, h, cellSize, cellSize)
        tileLayer.name = name
        layers.add(tileLayer)
        for (x in 0 until w) {
            for (y in 0 until h) {
                // Fills the layer with cells - tiles should then be assigned to the cells.
                val cell = TiledMapTileLayer.Cell()
                tileLayer.setCell(x, y, cell)
            }
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

    private fun setTerrain(x: Int, y: Int, terrain: Terrains) {
        grid[x][y].terrain = terrain
        terrainLayer.getCell(x, y).tile = terrainSet.getTile(terrain.ordinal)
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
        for (x in 0 until mapW) {
            for (y in 0 until mapH) {
                grid[x][y].cost = grid[x][y].terrain.moveCosts[unit.moveType]
            }
        }
    }

    private fun displayRanges(unit: AUnit) {
        val moveVectors = manRange(unit.vector, 0, unit.movesLeft)

        moveVectors.forEach { vec ->
            setCosts(unit)
            val path = AStar.findPath(grid[unit.vector.x][unit.vector.y], grid[vec.x][vec.y])
            // TODO: Incorporate movesLeft into AStar.findPath().
            if (path != null && path.cost <= unit.movesLeft) {
                val destination = grid[vec.x][vec.y]
                val cell = moveRangeLayer.getCell(vec.x, vec.y)
                // Destination conditions:
                // 1: Cell is empty, or occupied by this unit.
                if (destination.unit == null || destination.unit == unit) {
                    // Set a movement range tile, store for later retrieval and clearance.
                    cell.tile = rangesSet.getTile(RangeTiles.MOVE.ordinal)
                    // TODO: rangetables.mdestinationcells[vec] = cell
                }
                // TODO:
                //  2: Cell is occupied by a boardable unit.
                // 3: Allow ONLY PASSAGE for units of the same or allied teams.
                else if (destination.unit?.team == unit.team) { // TODO: or allies
                    cell.tile = rangesSet.getTile(RangeTiles.SELECT.ordinal)
                    // TODO: rangetables.mpassagecells[vec] = cell
                }
            }
        }
    }

    // TODO: selectNext() belongs here for now. May listen to InputMap signal.
    fun selectNext() {
        val gridReference = grid[short(cursor.actor.x)][short(cursor.actor.y)]
        gridReference.unit?.run {
            displayRanges(this)
        }
    }

    private fun placeUnit(unit: AUnit) {
        // When I did this in the AUnit init I was warned that I was leaking "this", which is fair.
        grid[unit.vector.x][unit.vector.y].unit = unit
    }

}