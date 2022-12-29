package com.example.classic

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.example.api.GameMode
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class ClassicModePlugin(wrapper: PluginWrapper) : Plugin(wrapper) {
    override fun start() {
        println("Classic mode start")
    }

    override fun stop() {
        println("Classic mode stop")
    }

    @Extension
    class ClassicMode : GameMode {
        override fun setAlphaTest(spriteBatch: SpriteBatch, newAlpha: Float) {
            spriteBatch.setColor(1.0f, 1.0f, 1.0f, newAlpha)
        }
    }
}