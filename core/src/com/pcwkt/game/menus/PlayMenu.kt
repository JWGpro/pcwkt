package com.pcwkt.game.menus

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import com.pcwkt.game.ingame.InGameScreen

class PlayMenu(game: Game, skin: Skin) : Menu(game) {

    init {
        // Label.
        val title = Label("Playing Screen", skin, "big")
        title.setAlignment(Align.center)
        title.y = (Gdx.graphics.height * 2 / 3).toFloat()
        title.width = Gdx.graphics.width.toFloat()
        stage.addActor(title)

        // Start button.
        val startButton = TextButton("Start", skin)
        startButton.width = (Gdx.graphics.width / 2).toFloat()
        startButton.setPosition(
            Gdx.graphics.width / 2 - startButton.width / 2,
            Gdx.graphics.height / 4 - startButton.height / 2
        )
        startButton.addListener(object : InputListener() {
            override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                dispose()
                game.screen = InGameScreen(game, "Classic", "Play")
            }

            override fun touchDown(
                event: InputEvent,
                x: Float,
                y: Float,
                pointer: Int,
                button: Int
            ): Boolean {
                return true
            }
        })
        stage.addActor(startButton)
    }

}