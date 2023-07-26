package com.pcwkt.game.ingame

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.input.GestureDetector.GestureListener
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.example.api.GameMode
import org.pf4j.CompoundPluginDescriptorFinder
import org.pf4j.DefaultPluginManager
import org.pf4j.ManifestPluginDescriptorFinder
import org.pf4j.PluginWrapper
import java.nio.file.Path

class InGameScreen(game: Game, parentMode: String, childMode: String) : Screen, InputProcessor,
    GestureListener {

    private val gameCamera = OrthographicCamera()
    private val uiCamera = OrthographicCamera()
    private val gameStage = Stage(ScreenViewport(gameCamera))
    private val uiStage =
        Stage(FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat(), uiCamera))
    private val assetManager = AssetManager(ExternalFileHandleResolver())
    private val tiledMap = TiledMap()
    private val tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)

    // An InputMultiplexer will pass input events to the first argument first and the last argument last.
    // The idea is that stuff on top (like UI) should receive it first.
    private val im = InputMultiplexer(uiStage, gameStage, this, GestureDetector(this))

    private var gameMode: GameMode

    private class PluginManager(importPaths: List<Path>) : DefaultPluginManager(importPaths) {
        override fun createPluginDescriptorFinder(): CompoundPluginDescriptorFinder {
            return CompoundPluginDescriptorFinder()
                .add(ManifestPluginDescriptorFinder())
        }
    }

    init {
        Gdx.input.inputProcessor = im

        val defaultPluginsDir = "${System.getProperty("user.home")}/pcwkt/gamemodes"
        val pluginsDir = System.getProperty("pf4j.pluginsDir", defaultPluginsDir)
        println("plugins dir: $pluginsDir")

        // create the plugin manager
        val pluginManager = PluginManager(listOf(Path.of(pluginsDir)))
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
        // TODO: Some kind of error message if no modes
        gameMode = gameModes.firstOrNull()!!
        // TODO: Is there a security concern in passing assetManager?
        //  You can't reassign the resolver to an internal one, at least.
        //  (Yeah but an internal one is internal to HERE anyway, it's not the filesystem, duh.)
        //  This is also a consideration when exposing libGDX as a dependency for plugins.
        //  Well, they can import anything they want. It's up to us to restrict permissions.
        gameMode.gameInit(
            gameStage,
            assetManager,
            tiledMap,
            gameCamera
        )

        // print extensions for each started plugin
        val startedPlugins: List<PluginWrapper> = pluginManager.startedPlugins
        startedPlugins.forEach { plugin ->
            val pluginId: String = plugin.descriptor.pluginId
            println(String.format("Extensions added by plugin '%s':", pluginId))
        }

    }

    override fun show() {
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        gameMode.gameLoop(delta)

        gameCamera.update()
        tiledMapRenderer.setView(gameCamera)
        tiledMapRenderer.render()

        gameStage.act(delta)
        uiStage.act(delta)

        gameStage.draw()
        uiStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        gameStage.viewport.update(width, height, false)
        uiStage.viewport.update(width, height, false)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
        gameStage.dispose()
        uiStage.dispose()
        assetManager.dispose()
        tiledMap.dispose()
    }

    // Input event handling

    override fun keyDown(keycode: Int): Boolean {
        return gameMode.keyDown(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        return gameMode.keyUp(keycode)
    }

    override fun keyTyped(character: Char): Boolean {
        return gameMode.keyTyped(character)
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return gameMode.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return gameMode.touchUp(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return gameMode.touchDragged(screenX, screenY, pointer)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return gameMode.mouseMoved(screenX, screenY)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return gameMode.scrolled(amountX, amountY)
    }

    // Touch gesture detector events

    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        println("PCWKT: this was a different touchDown event that took floats")
        return gameMode.touchDown(x, y, pointer, button)
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        return gameMode.tap(x, y, count, button)
    }

    override fun longPress(x: Float, y: Float): Boolean {
        return gameMode.longPress(x, y)
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        return gameMode.fling(velocityX, velocityY, button)
    }

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        return gameMode.pan(x, y, deltaX, deltaY)
    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return gameMode.panStop(x, y, pointer, button)
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        return gameMode.zoom(initialDistance, distance)
    }

    override fun pinch(
        initialPointer1: Vector2?,
        initialPointer2: Vector2?,
        pointer1: Vector2?,
        pointer2: Vector2?
    ): Boolean {
        return gameMode.pinch(initialPointer1, initialPointer2, pointer1, pointer2)
    }

    override fun pinchStop() {
        gameMode.pinchStop()
    }
}