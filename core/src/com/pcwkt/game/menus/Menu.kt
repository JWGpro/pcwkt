package com.pcwkt.game.menus

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport

abstract class Menu(
    protected val game: Game,
    protected val stage: Stage = Stage(ScreenViewport())
) : Screen {

    override fun show() {
        // On RMB,
        stage.addListener(object : ClickListener(Input.Buttons.RIGHT) {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                // TODO: Go to the parent menu.
                println("Menu RMB clicked...")
//                dispose()
//                game.screen = MainMenu(game)
            }
        })

        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, false)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
        stage.dispose()
    }
}