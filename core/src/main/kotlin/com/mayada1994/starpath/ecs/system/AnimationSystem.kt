package com.mayada1994.starpath.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.GdxRuntimeException
import com.mayada1994.starpath.ecs.component.AnimationComponent
import com.mayada1994.starpath.ecs.component.AnimationComponent.Animation2D
import com.mayada1994.starpath.ecs.component.AnimationComponent.AnimationType
import com.mayada1994.starpath.ecs.component.GraphicComponent
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import java.util.*

class AnimationSystem(private val atlas: TextureAtlas) : IteratingSystem(allOf(AnimationComponent::class, GraphicComponent::class).get()), EntityListener {

    private val animationCache = EnumMap<AnimationType, Animation2D>(AnimationType::class.java)

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        engine.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        engine.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity) {
        entity[AnimationComponent.mapper]?.let { aniCmp ->
            aniCmp.animation = getAnimation(aniCmp.type)
            val frame = aniCmp.animation.getKeyFrame(aniCmp.stateTime)
            entity[GraphicComponent.mapper]?.setSpriteRegion(frame)
        }
    }

    override fun entityRemoved(entity: Entity) = Unit

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val aniCmp = entity[AnimationComponent.mapper]
        require(aniCmp != null) { "Entity |entity| must have an AnimationComponent. entity=$entity" }
        val graphic = entity[GraphicComponent.mapper]
        require(graphic != null) { "Entity |entity| must have a GraphicComponent. entity=$entity" }

        if (aniCmp.type == AnimationType.NONE) {
            LOG.error { "No aniCmp type specified" }
            return
        }

        if (aniCmp.animation.type == aniCmp.type) {
            // animation is correct -> update it
            aniCmp.stateTime += deltaTime
        } else {
            // change animation
            aniCmp.stateTime = 0f
            aniCmp.animation = getAnimation(aniCmp.type)
        }

        val frame = aniCmp.animation.getKeyFrame(aniCmp.stateTime)
        graphic.setSpriteRegion(frame)
    }

    private fun getAnimation(type: AnimationType): Animation2D {
        var animation = animationCache[type]
        if (animation == null) {
            val regions = atlas.findRegions(type.atlasKey)
            if (regions.isEmpty) {
                LOG.error { "No regions found for ${type.atlasKey}" }
                throw GdxRuntimeException("There is no animation region in the game atlas")
            } else {
                LOG.debug { "Adding animation of type $type with ${regions.size} regions" }
            }
            animation = Animation2D(type, regions, type.playMode, type.speedRate)
            animationCache[type] = animation
        }
        return animation
    }

    companion object {
        private val LOG = logger<AnimationSystem>()
    }

}