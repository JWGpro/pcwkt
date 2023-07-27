package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.example.api.MapActorA

class Cursor(gameStage: Stage, assetManager: AssetManager) {
    private val atlas = Assets.TextureAtlases.ANIMS
    val actor = MapActorA(
        assetManager.get(atlas.path),
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