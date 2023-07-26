package com.example.api

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import org.pf4j.ExtensionPoint

interface GameMode : ExtensionPoint {
    fun gameInit(
        gameStage: Stage,
        assetManager: AssetManager,
        tiledMap: TiledMap,
        gameCamera: OrthographicCamera
    )

    fun gameLoop(deltaTime: Float)

    // Input
    fun keyDown(keycode: Int): Boolean
    fun keyUp(keycode: Int): Boolean
    fun keyTyped(character: Char): Boolean
    fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean
    fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean
    fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean
    fun mouseMoved(screenX: Int, screenY: Int): Boolean
    fun scrolled(amountX: Float, amountY: Float): Boolean

    // Touch gesture events
    fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean
    fun tap(x: Float, y: Float, count: Int, button: Int): Boolean
    fun longPress(x: Float, y: Float): Boolean
    fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean
    fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean
    fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean
    fun zoom(initialDistance: Float, distance: Float): Boolean
    fun pinch(
        initialPointer1: Vector2?,
        initialPointer2: Vector2?,
        pointer1: Vector2?,
        pointer2: Vector2?
    ): Boolean

    fun pinchStop()

}
