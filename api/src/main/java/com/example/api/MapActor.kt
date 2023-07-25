package com.example.api

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor

class MapActor(private val region: TextureRegion) : Actor() {

    var alpha: Float = 1.0f

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.setColor(color.r, color.g, color.b, color.a * parentAlpha * alpha)
        batch?.draw(region, x, y)
    }
}