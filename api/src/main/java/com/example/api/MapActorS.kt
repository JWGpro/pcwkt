package com.example.api

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction

// "Static" or non-animated
class MapActorS(private val thisStage: Stage, private val region: TextureRegion) : Actor() {

    private var alpha = 1.0f

    // TODO: Fluff. Could just have the caller pass MoveToActions.
    //  But allocations are happening either way. Again, think pooling.
    class RouteStep(val x: Float, val y: Float, val duration: Float)

    fun moveTo(routeSteps: List<RouteStep>, after: () -> Unit) {
        // TODO: Pool
        val sequenceAction = SequenceAction()

        routeSteps.forEach {
            sequenceAction.addAction(Actions.moveTo(it.x, it.y, it.duration))
        }
        sequenceAction.addAction(object : RunnableAction() {
            override fun run() {
                after()
            }
        })

        this.addAction(sequenceAction)
    }

    fun hide() {
        this.remove()
    }

    fun unhide() {
        // Ends up on top of everything where it wasn't before, but that shouldn't be a problem.
        thisStage.addActor(this)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.setColor(color.r, color.g, color.b, color.a * parentAlpha * alpha)
        batch?.draw(region, x, y)
    }
}