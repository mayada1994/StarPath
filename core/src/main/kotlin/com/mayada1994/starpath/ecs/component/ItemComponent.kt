package com.mayada1994.starpath.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pool
import com.mayada1994.starpath.ecs.component.AnimationComponent.AnimationType
import ktx.ashley.mapperFor

class ItemComponent : Component, Pool.Poolable {

    var type: ItemType = ItemType.None

    sealed class ItemType(open val atlasKey: String = "") {
        object None : ItemType()
        data class Bonus(override val atlasKey: String, val bonusPoints: Int) : ItemType()
        data class Boost(override val atlasKey: String, val boostValue: Int) : ItemType()
        data class Damage(val animationType: AnimationType) : ItemType()
    }

    override fun reset() {
        type = ItemType.None
    }

    companion object {
        val mapper = mapperFor<ItemComponent>()

        val randomBoost: ItemType.Boost
            get() = boostItems[MathUtils.random(0, boostItems.size - 1)]

        val randomBonus: ItemType.Bonus
            get() = bonusItems[MathUtils.random(0, bonusItems.size - 1)]

        val randomDamage: ItemType.Damage
            get() = damageItems[MathUtils.random(0, damageItems.size - 1)]

        private val boostItems = listOf(ItemType.Boost("boost", 5))

        private val bonusItems = listOf(
            ItemType.Bonus("star_orange", 5),
            ItemType.Bonus("star_yellow", 10),
            ItemType.Bonus("star_blue", 25)
        )

        private val damageItems = listOf(
            ItemType.Damage(AnimationType.METEORITE_BLUE),
            ItemType.Damage(AnimationType.METEORITE_RED),
            ItemType.Damage(AnimationType.METEORITE_YELLOW),
            ItemType.Damage(AnimationType.METEORITE_ORANGE),
            ItemType.Damage(AnimationType.METEORITE_WHITE)
        )
    }
}