package com.example.classic

import com.badlogic.gdx.Gdx
import com.example.classic.serial.AUnitSerial
import com.example.classic.serial.GridRefSerial
import com.example.classic.serial.TerrainSerial
import com.example.classic.terrains.Property
import com.example.classic.terrains.TerrainType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val format = Json { explicitNulls = false }

class MapLoader {
    fun loadMap(): List<List<GridRefSerial>> {
        val mapJson = Gdx.files.external("pcwkt/maps/map.json").readString()
        return format.decodeFromString(mapJson)
    }

    fun saveMap(grid: Array<Array<MapManager.GridReference>>) {
        // TODO: Likely that plugins will not have filesystem write permissions, so this will really
        //  go through API. Make sure you don't expose any vulns in that as well, though.

        val gridSerial = grid.map { column ->
            column.map { cell ->
                val unit =
                    if (cell.unit == null) null else AUnitSerial(cell.unit!!.type, cell.unit!!.team)

                // TODO: no
                if (cell.terrain.isProperty) {
                    val terrain = cell.terrain as Property
                    GridRefSerial(unit, TerrainSerial(terrain.type, terrain.team))
                } else {
                    val terrain = cell.terrain as TerrainType
                    GridRefSerial(unit, TerrainSerial(terrain, null))
                }
            }
        }
        Gdx.files.external("pcwkt/maps/map.json")
            .writeString(format.encodeToString(gridSerial), false)
    }
}