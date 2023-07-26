package com.example.api

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor

// "Static" or non-animated
class MapActorS(private val region: TextureRegion) : Actor() {

    private var alpha = 1.0f

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.setColor(color.r, color.g, color.b, color.a * parentAlpha * alpha)
        batch?.draw(region, x, y)
    }
}