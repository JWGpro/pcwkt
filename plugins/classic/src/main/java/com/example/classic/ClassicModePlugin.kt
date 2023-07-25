package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.example.api.GameMode
import com.example.api.MapActor
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class ClassicModePlugin(wrapper: PluginWrapper) : Plugin(wrapper) {
    override fun start() {
        println("Classic mode start")
    }

    override fun stop() {
        println("Classic mode stop")
    }

    @Extension
    class ClassicMode : GameMode {
        private val actor = MapActor(TextureRegion(Texture("badlogic.jpg")))
        private var toggle = false

        override fun gameInit(gameStage: Stage, assetManager: AssetManager) {
            println("Classic mode gameInit")

            Assets.loadAll(assetManager)

            actor.setPosition(0f, 0f)
            gameStage.addActor(actor)
        }

        override fun gameLoop(deltaTime: Float) {
//            println("Classic mode gameLoop")
            actor.alpha = if (toggle) 1.0f else 0.0f
            toggle = !toggle
        }

        override fun keyDown(keycode: Int): Boolean {
            println("Classic mode keyDown")
            return true;
        }

        override fun keyUp(keycode: Int): Boolean {
            println("Classic mode keyUp")
            return true;
        }

        override fun keyTyped(character: Char): Boolean {
            println("Classic mode keyTyped")
            return true;
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            println("Classic mode touchDown")
            return true;
        }

        override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
            println("Classic mode touchDown")
            return true;
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            println("Classic mode touchUp")
            return true;
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            println("Classic mode touchDragged")
            return true;
        }

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            println("Classic mode mouseMoved")
            actor.setPosition(screenX.toFloat(), screenY.toFloat())
            return true;
        }

        override fun scrolled(amountX: Float, amountY: Float): Boolean {
            println("Classic mode scrolled")
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