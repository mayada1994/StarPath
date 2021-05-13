package com.mayada1994.starpath.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktx.collections.GdxArray

class AnimationComponent : Component, Pool.Poolable {

    var type = AnimationType.NONE
    var stateTime = 0f
    lateinit var animation: Animation2D

    enum class AnimationType(
            val atlasKey: String = "",
            val playMode: Animation.PlayMode = Animation.PlayMode.LOOP,
            val speedRate: Float = 0.3f
    ) {
        NONE,
        METEORITE_BLUE("meteorite_blue"),
        METEORITE_RED("meteorite_red"),
        METEORITE_YELLOW("meteorite_yellow"),
        METEORITE_ORANGE("meteorite_orange"),
        METEORITE_WHITE("meteorite_white"),
        EXPLOSION("explosion", Animation.PlayMode.NORMAL)
    }

    class Animation2D(
            val type: AnimationType,
            keyFrames: GdxArray<out TextureRegion>,
            playMode: PlayMode = PlayMode.LOOP,
            speed: Float = 1f
    ) : Animation<TextureRegion>((DEFAULT_FRAME_DURATION) / speed, keyFrames, playMode)

    override fun reset() {
        type = AnimationType.NONE
        stateTime = 0f
    }

    companion object {
        val mapper = mapperFor<AnimationComponent>()
        private const val DEFAULT_FRAME_DURATION = 1 / 20f
    }

}