package com.example.api

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor

// Animated
class MapActorA(
    atlas: TextureAtlas,
    animationName: String,
    frameTime: Float,
    playMode: Animation.PlayMode
) : Actor() {

    private val animation =
        Animation<TextureRegion>(frameTime, atlas.findRegions(animationName), playMode)

    private var stateTime = 0.0f
    private var alpha = 1.0f

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.setColor(color.r, color.g, color.b, color.a * parentAlpha * alpha)
        batch?.draw(animation.getKeyFrame(stateTime), x, y)
    }

    override fun act(delta: Float) {
        super.act(delta)
        stateTime += delta
    }
}