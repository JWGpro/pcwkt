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
import com.example.classic.units.AUnit
import com.example.classic.units.Infantry
import kotlin.math.floor

private const val EXT = "pcwkt"
private const val CELL_SIZE = 16


class MapManager(
    private val gameStage: Stage,
    private val assetManager: AssetManager,
    private val tiledMap: TiledMap,
    private val cursor: Cursor
) {
    class GridReference(
        var vector: CellVector,
        var unit: AUnit?,
        var targets: Array<AUnit>?
    )

    // TODO: These should come from the loaded map
    private val mapW = 30
    private val mapH = 20
    val grid = Array(mapW) { Array(mapH) { GridReference(CellVector(0, 0), null, null) } }
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

//        spawnInfantry(13, 7, Team.RED)
        Infantry(13, 7, Team.RED)
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
        terrainLayer.getCell(x, y).tile = terrainSet.getTile(terrain.ordinal)
    }

    private fun displayRanges(unit: AUnit) {
        // TODO:
    }

    // TODO: selectNext() belongs here for now. May listen to InputMap signal.
    fun selectNext() {
        val gridReference = grid[short(cursor.actor.x)][short(cursor.actor.y)]
        gridReference.unit?.run {
            displayRanges(this)
        }
    }

}