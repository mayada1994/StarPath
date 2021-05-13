package com.mayada1994.starpath.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class PlayerComponent : Component, Pool.Poolable {
    var points = 0
    var distance = 0f

    override fun reset() {
        points = 0
        distance = 0f
    }


    companion object {
        val mapper = mapperFor<PlayerComponent>()
    }

}