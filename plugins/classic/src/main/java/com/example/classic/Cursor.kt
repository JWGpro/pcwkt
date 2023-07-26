package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.example.api.MapActorA

class Cursor(gameStage: Stage, assetManager: AssetManager) {
    private val atlas = Assets.TextureAtlases.ANIMS
    private val actor = MapActorA(
        // TODO: Gotta do something about this pcwkt/ stuff
        assetManager.get("pcwkt/${atlas.path}"),
        "cursoridle",
        0.5f,
        Animation.PlayMode.LOOP
    )

    init {
        setPosition(0f, 0f)
        gameStage.addActor(actor)
    }

    fun setPosition(x: Float, y: Float) {
        actor.setPosition(x, y)
    }
}