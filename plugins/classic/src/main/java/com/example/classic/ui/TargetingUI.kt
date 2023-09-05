package com.example.classic.ui

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.example.classic.Assets

class TargetingUI(
    private val uiStage: Stage,
    assetManager: AssetManager,
) {

    // Front-loading everything. Trying to avoid dynamic allocation
    private val container = Table()
    private val unitLabel = Label("label", assetManager.get<Skin>(Assets.Skins.DEFAULT.path))

    init {
        container.setFillParent(true)
        container.add(unitLabel)

        // TODO: Some kind of Observer updating a simulated battle display
        unitLabel.setText("Hello world")
    }

    fun show() {
        uiStage.addActor(container)
    }

    fun clear() {
        container.remove()
    }

}