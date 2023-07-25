package com.pcwkt.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.pcwkt.game.menus.PlayMenu

class PCWKTGame : Game() {
    private lateinit var skin: Skin

    override fun create() {
        // TODO: Load skin externally
        skin = Skin(Gdx.files.internal("skins/glassy/skin/glassy-ui.json"))

        this.setScreen(PlayMenu(this, skin))
    }

    override fun dispose() {
        skin.dispose()
    }
}