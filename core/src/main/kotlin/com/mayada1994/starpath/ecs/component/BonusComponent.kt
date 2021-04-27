package com.mayada1994.starpath.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class BonusComponent : Component, Pool.Poolable {

    var type = BonusType.NONE

    enum class BonusType(val atlasKey: String) {
        NONE(""),
        STAR_1("graphics/star_1.png"),
        STAR_2("graphics/star_2.png"),
        STAR_3("graphics/star_3.png"),
        BOOST("graphics/boost.png")
    }

    override fun reset() {
        type = BonusType.NONE
    }

    companion object {
        val mapper = mapperFor<BonusComponent>()
    }
}