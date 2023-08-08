package com.example.classic

import com.badlogic.gdx.Gdx
import com.example.classic.serial.AUnitSerial
import com.example.classic.serial.GridRefSerial
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MapLoader {
    fun loadMap(): List<List<GridRefSerial>> {
        val mapJson = Gdx.files.external("pcwkt/maps/map.json").readString()
        return Json.decodeFromString(mapJson)
    }

    fun saveMap(grid: Array<Array<MapManager.GridReference>>) {
        // TODO: Likely that plugins will not have filesystem write permissions, so this will really
        //  go through API. Make sure you don't expose any vulns in that as well, though.

        val gridSerial = grid.map { column ->
            column.map { cell ->
                val unit =
                    if (cell.unit == null) null else AUnitSerial(cell.unit!!.type, cell.unit!!.team)
                GridRefSerial(unit, cell.terrain)
            }
        }
        Gdx.files.external("pcwkt/maps/map.json")
            .writeString(Json.encodeToString(gridSerial), false)
    }
}