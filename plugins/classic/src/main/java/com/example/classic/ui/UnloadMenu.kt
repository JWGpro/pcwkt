package com.example.classic.ui

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.example.api.CyclingArray
import com.example.classic.Assets
import com.example.classic.ReplayManager
import com.example.classic.commands.action.UnloadCommand
import com.example.classic.selectionstate.ActingState
import com.example.classic.selectionstate.SelectionStateManager
import com.example.classic.selectionstate.UnloadedState
import com.example.classic.units.AUnit

// TODO: Unlikely to work this way at all. Mobius Front '83 had a good UI for this.
class UnloadMenu(
    private val uiStage: Stage,
    assetManager: AssetManager,
    private val selectionStateManager: SelectionStateManager,
    private val replayManager: ReplayManager
) {

    // Front-loading everything. Trying to avoid dynamic allocation
    private val container = Table()
    private val unitLabel = Label("label", assetManager.get<Skin>(Assets.Skins.DEFAULT.path))

    // Button to cycle unloadable units
    // TODO: Again, this is crap, not gonna stay like this
    private val unitButton = TextButton("Unit", assetManager.get<Skin>(Assets.Skins.DEFAULT.path))
    private val confirmButton =
        TextButton("Unload", assetManager.get<Skin>(Assets.Skins.DEFAULT.path))

    private var actingState: ActingState? = null
    private var outunit: AUnit? = null
    private var outunits: CyclingArray<AUnit>? = null

    init {
        container.setFillParent(true)
        container.add(unitLabel)
        container.row()
        container.add(unitButton)
        container.row()
        container.add(confirmButton)

        unitButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                update()
            }
        })
        confirmButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                toUnloadedState()
            }
        })
    }

    fun show(transport: AUnit, actingState: ActingState) {
        this.actingState = actingState
        // yes dynamic allocation ok
        outunits = CyclingArray(transport.boardedUnits.toTypedArray())
        update()

        uiStage.addActor(container)
    }

    fun clear() {
        // Let's see if we need to null anything.
//        actingState = null
//        outunits = null
//        outunit = null

        container.remove()
    }

    private fun update() {
        outunit = outunits!!.cycle(backwards = true)

        unitLabel.setText("${outunit!!.type.name} - ${outunit!!.movesLeft}")
    }

    private fun toUnloadedState() {
        val stack = actingState!!.stack
        stack.addLast(actingState!!)

        val unloadCommand = UnloadCommand(outunit!!)
        unloadCommand.execute()
        // WARNING: This will clear() UnloadMenu on init!
        val unloadedState =
            UnloadedState(stack, outunit!!, replayManager, unloadCommand, actingState!!)

        selectionStateManager.state = unloadedState
    }
}