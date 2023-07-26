package com.example.classic

import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileSet
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile

private const val CELL_SIZE = 16

class MapManager(private val tiledMap: TiledMap) {
    private val mapW = 30
    private val mapH = 20
    private val terrainLayer = newMapLayer("terrain", mapW, mapH, CELL_SIZE)
    private val terrainSet = newTileSet("terrain")

    init {
        // Generate tileset
        Terrains.values().forEach { terrain ->
            val tile = newStaticTile("terrain-assets/default/${terrain.path}")
            terrainSet.putTile(terrain.ordinal, tile)
        }

        // Init grid and terrain
        for (x in 0 until mapW) {
            for (y in 0 until mapH) {
                setTerrain(x, y, Terrains.SEA)
            }
        }

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
        val fh = ExternalFileHandleResolver().resolve("pcwkt/$path")
        return StaticTiledMapTile(TextureRegion(Texture(fh)))
    }

    private fun setTerrain(x: Int, y: Int, terrain: Terrains) {
        terrainLayer.getCell(x, y).tile = terrainSet.getTile(terrain.ordinal)
    }

}