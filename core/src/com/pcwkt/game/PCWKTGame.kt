package com.pcwkt.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.example.api.GameMode
import org.pf4j.CompoundPluginDescriptorFinder
import org.pf4j.DefaultPluginManager
import org.pf4j.ManifestPluginDescriptorFinder
import org.pf4j.PluginWrapper
import java.nio.file.Path
import kotlin.io.path.Path

class PCWKTGame : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var img: Texture

    private class PluginManager(importPaths: List<Path>) : DefaultPluginManager(importPaths) {
        override fun createPluginDescriptorFinder(): CompoundPluginDescriptorFinder {
            return CompoundPluginDescriptorFinder()
                .add(ManifestPluginDescriptorFinder());
        }
    }

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")

        val defaultPluginsDir = Gdx.files.external("pcwkt/plugins").file().absolutePath
        val pluginsDir = defaultPluginsDir
//        val pluginsDir = System.getProperty("pf4j.pluginsDir", defaultPluginsDir)
        println("plugins dir: $pluginsDir")

        // create the plugin manager
        val pluginManager = PluginManager(listOf(Path(pluginsDir)))
        // load the plugins
        pluginManager.loadPlugins()
        // start (active/resolved) the plugins
        pluginManager.startPlugins()

        // retrieves the extensions for GameMode extension point
        val gameModes: List<GameMode> = pluginManager.getExtensions(GameMode::class.java)
        println(
            String.format(
                "Found %d extensions for extension point '%s'",
                gameModes.size,
                GameMode::class.java.name
            )
        )
        gameModes.firstOrNull()?.setAlphaTest(batch, 0.1f)

        // print extensions for each started plugin
        val startedPlugins: List<PluginWrapper> = pluginManager.startedPlugins
        startedPlugins.forEach { plugin ->
            val pluginId: String = plugin.descriptor.pluginId
            println(String.format("Extensions added by plugin '%s':", pluginId))
        }
    }

    override fun render() {
        ScreenUtils.clear(1f, 0f, 0f, 1f)
        batch.begin()
        batch.draw(img, 0f, 0f)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }
}