package com.mayada1994.starpath.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.mayada1994.starpath.ecs.component.GraphicComponent
import com.mayada1994.starpath.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.error
import ktx.log.logger

class RenderSystem(
        private val batch: Batch,
        private val gameViewport: Viewport
) : SortedIteratingSystem(
        allOf(TransformComponent::class, GraphicComponent::class).get(),
        compareBy { entity -> entity[TransformComponent.mapper] }
) {

    override fun update(deltaTime: Float) {
        forceSort()
        gameViewport.apply()
        batch.use(gameViewport.camera.combined) {
            super.update(deltaTime)
        }

    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have a TransformComponent. entity=$entity" }

        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null) { "Entity |entity| must have a GraphicComponent. entity=$entity" }

        if (graphic.sprite.texture == null) {
            LOG.error {
                "Entity has no texture for rendering. entity=$entity"
            }
            return
        }

        graphic.sprite.run {
            rotation = transform.rotationDeg
            setBounds(transform.interpolatedPosition.x, transform.position.y, transform.size.x, transform.size.y)
            draw(batch)
        }

    }

    companion object {
        private val LOG = logger<RenderSystem>()
    }
}