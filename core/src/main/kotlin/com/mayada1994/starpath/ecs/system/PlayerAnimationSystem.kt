package com.mayada1994.starpath.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.mayada1994.starpath.ecs.component.FacingComponent
import com.mayada1994.starpath.ecs.component.FacingComponent.FacingDirection
import com.mayada1994.starpath.ecs.component.GraphicComponent
import com.mayada1994.starpath.ecs.component.PlayerComponent
import com.mayada1994.starpath.ecs.component.TransformComponent
import ktx.ashley.allOf
import ktx.ashley.get

class PlayerAnimationSystem(
        private val defaultRegion: TextureRegion,
        private val leftRegion: TextureRegion,
        private val rightRegion: TextureRegion,
) : IteratingSystem(allOf(PlayerComponent::class, FacingComponent::class, GraphicComponent::class).get()), EntityListener {
    private var lastDirection = FacingDirection.DEFAULT

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        entity[GraphicComponent.mapper]?.setSpriteRegion(defaultRegion)
        entity[TransformComponent.mapper]?.let {
            it.size.set(3f, 3f)
            it.position.set(3f, 0f, 0f)
        }
    }

    override fun entityRemoved(entity: Entity) = Unit

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val facing = entity[FacingComponent.mapper]
        require(facing != null) { "Entity |entity| must have a FacingComponent. entity=$entity" }

        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null) { "Entity |entity| must have a GraphicComponent. entity=$entity" }

        if (facing.direction == lastDirection && graphic.sprite.texture != null) {
            //do nothing as the texture is already set and the direction didn't change
            return
        }

        lastDirection = facing.direction
        val region = when (facing.direction) {
            FacingDirection.LEFT -> leftRegion
            FacingDirection.RIGHT -> rightRegion
            else -> defaultRegion
        }
        graphic.setSpriteRegion(region)
    }

}