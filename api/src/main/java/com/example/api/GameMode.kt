package com.example.api

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.pf4j.ExtensionPoint

interface GameMode : ExtensionPoint {
    fun setAlphaTest(spriteBatch: SpriteBatch, newAlpha: Float)
}
