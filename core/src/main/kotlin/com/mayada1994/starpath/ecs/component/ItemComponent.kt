package com.mayada1994.starpath.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class ItemComponent : Component, Pool.Poolable {

    var type: ItemType = ItemType.None

    sealed class ItemType(open val atlasKey: String = "") {
        object None : ItemType()
        data class Bonus(override val atlasKey: String, val bonusPoints: Int) : ItemType()
        data class Boost(override val atlasKey: String, val boostValue: Int) : ItemType()
        data class Damage(override val atlasKey: String) : ItemType()
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
                ItemType.Damage("meteorite_blue"),
                ItemType.Damage("meteorite_red"),
                ItemType.Damage("meteorite_yellow"),
                ItemType.Damage("meteorite_orange"),
                ItemType.Damage("meteorite_white")
        )
    }
}