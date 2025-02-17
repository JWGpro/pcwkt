package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.example.api.GameMode
import com.example.classic.selectionstate.SelectionStateManager
import com.example.classic.ui.ActionMenu
import com.example.classic.ui.DeployMenu
import com.example.classic.ui.TargetingUI
import com.example.classic.ui.UnloadMenu
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import kotlin.math.pow

class ClassicModePlugin(wrapper: PluginWrapper) : Plugin(wrapper) {
    override fun start() {
        println("Classic mode start")
    }

    override fun stop() {
        println("Classic mode stop")
    }

    @Extension
    class ClassicMode : GameMode {
        private lateinit var gameCamera: OrthographicCamera
        private var lastX = 0
        private var lastY = 0
        private lateinit var mapManager: MapManager

        override fun gameInit(
            gameStage: Stage,
            uiStage: Stage,
            assetManager: AssetManager,
            tiledMap: TiledMap,
            gameCamera: OrthographicCamera,
            uiCamera: OrthographicCamera
        ) {
            this.gameCamera = gameCamera
            // TODO: Center map properly
            gameCamera.translate(-400f, -200f)
            gameCamera.zoom = 0.5f

            Assets.loadAll(assetManager)

            val replayManager = ReplayManager()
            val turnManager = TurnManager(replayManager)
            val cursor = Cursor(gameStage, assetManager)

            val mapLoader = MapLoader()
            mapManager = MapManager(
                assetManager,
                gameStage,
                tiledMap,
                cursor,
                turnManager,
                mapLoader.loadMap()
            )

            // This needs to last indefinitely, so something needs to hold onto it
            val selectionStateManager = SelectionStateManager(mapManager)
            val unloadMenu = UnloadMenu(uiStage, assetManager, selectionStateManager, replayManager)
            val targetingUI = TargetingUI(uiStage, assetManager)
            val actionMenu =
                ActionMenu(
                    uiStage,
                    assetManager,
                    mapManager,
                    selectionStateManager,
                    replayManager,
                    unloadMenu,
                    targetingUI
                )

            ServiceLocator.mapManager = mapManager
            ServiceLocator.actionMenu = actionMenu
            ServiceLocator.unloadMenu = unloadMenu
            ServiceLocator.turnManager = turnManager

            val deployMenu = DeployMenu(uiStage, assetManager)
            ServiceLocator.deployMenu = deployMenu

            selectionStateManager.defaultState()
        }

        override fun gameLoop(deltaTime: Float) {
            // TODO: Consider inline functions for costly operations on each frame
        }

        override fun keyDown(keycode: Int): Boolean {
            InputHandler.tryBind(InputHandler.Binds.KEY_DOWN, keycode)
            return true;
        }

        override fun keyUp(keycode: Int): Boolean {
            return true;
        }

        override fun keyTyped(character: Char): Boolean {
            return true;
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            // Set for panning; init pan.
            lastX = screenX
            lastY = screenY

            return true;
        }

        override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
            println("Classic mode touchDown")
            return true;
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            InputHandler.tryBind(InputHandler.Binds.TOUCH_UP, button)

            return true;
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            val deltaX = screenX - lastX
            val deltaY = lastY - screenY
            lastX = screenX
            lastY = screenY

            // It appears that you need to set a boolean to pan with MMB,
            //  because there is no parameter for the button used to drag.
            // That param is in touchUp and touchDown, so they can set that boolean.
            gameCamera.translate(deltaX.toFloat(), deltaY.toFloat())

            return true;
        }

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            val offX = gameCamera.viewportWidth / 2
            val offY = gameCamera.viewportHeight / 2

            val adjustedX = gameCamera.position.x - ((offX - screenX) * gameCamera.zoom)
            val adjustedY = gameCamera.position.y - ((screenY - offY) * gameCamera.zoom)

            mapManager.updateCursor(adjustedX, adjustedY)

            return true;
        }

        override fun scrolled(amountX: Float, amountY: Float): Boolean {
            // TODO: InputMap
            gameCamera.zoom *= (1.5f).pow(amountY)

            return true;
        }

        override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
            println("Classic mode tap")
            return true;
        }

        override fun longPress(x: Float, y: Float): Boolean {
            println("Classic mode longPress")
            return true;
        }

        override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
            println("Classic mode fling")
            return true;
        }

        override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
            println("Classic mode pan")
            return true;
        }

        override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
            println("Classic mode panStop")
            return true;
        }

        override fun zoom(initialDistance: Float, distance: Float): Boolean {
            println("Classic mode zoom")
            return true;
        }

        override fun pinch(
            initialPointer1: Vector2?,
            initialPointer2: Vector2?,
            pointer1: Vector2?,
            pointer2: Vector2?
        ): Boolean {
            println("Classic mode pinch")
            return true;
        }

        override fun pinchStop() {
            println("Classic mode pinchStop")
        }
    }
}