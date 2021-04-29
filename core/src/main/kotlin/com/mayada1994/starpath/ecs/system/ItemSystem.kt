package com.mayada1994.starpath.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.ecs.component.*
import com.mayada1994.starpath.ecs.component.AnimationComponent.AnimationType
import com.mayada1994.starpath.ecs.component.ItemComponent.Companion.randomBonus
import com.mayada1994.starpath.ecs.component.ItemComponent.Companion.randomBoost
import com.mayada1994.starpath.ecs.component.ItemComponent.Companion.randomDamage
import com.mayada1994.starpath.ecs.component.ItemComponent.ItemType
import ktx.ashley.*
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.error
import ktx.log.logger

class ItemSystem(
        private val atlas: TextureAtlas
) : IteratingSystem(allOf(ItemComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()) {

    private class SpawnPattern(
            type1: ItemType = ItemType.None,
            type2: ItemType = ItemType.None,
            type3: ItemType = ItemType.None,
            type4: ItemType = ItemType.None,
            type5: ItemType = ItemType.None,
            val types: GdxArray<ItemType> = gdxArrayOf(type1, type2, type3, type4, type5)
    )

    private val playerBoundingRect = Rectangle()
    private val itemBoundingRect = Rectangle()
    private val playerEntities by lazy {
        engine.getEntitiesFor(
                allOf(PlayerComponent::class).exclude(RemoveComponent::class).get()
        )
    }
    private var spawnTime = 0f
    private val spawnPatterns = gdxArrayOf(
            SpawnPattern(type1 = randomBonus, type2 = randomDamage, type5 = randomBonus),
            SpawnPattern(type1 = randomBonus, type2 = randomBonus, type5 = randomDamage),
            SpawnPattern(type2 = randomDamage, type4 = randomBonus, type5 = randomBonus),
            SpawnPattern(type2 = randomBonus, type4 = randomBonus),
            SpawnPattern(type2 = randomBonus, type5 = randomDamage),
            SpawnPattern(type1 = randomBonus, type4 = randomBonus),
            SpawnPattern(type2 = randomDamage, type3 = randomBonus),
            SpawnPattern(type1 = randomBonus, type2 = randomBonus, type4 = randomDamage, type5 = randomBonus),
            SpawnPattern(type2 = randomBonus, type4 = randomBonus, type5 = randomBonus),
            SpawnPattern(type1 = randomBonus, type2 = randomBonus, type4 = randomBonus, type5 = randomBonus),
            SpawnPattern(type1 = randomDamage, type2 = randomDamage, type5 = randomDamage),
            SpawnPattern(type1 = randomDamage, type2 = randomDamage, type5 = randomBonus),
            SpawnPattern(type2 = randomBonus, type4 = randomDamage, type5 = randomDamage),
            SpawnPattern(type2 = randomBoost, type4 = randomDamage),
            SpawnPattern(type2 = randomDamage, type5 = randomBonus),
            SpawnPattern(type1 = randomDamage, type4 = randomDamage),
            SpawnPattern(type2 = randomDamage, type3 = randomDamage),
            SpawnPattern(type1 = randomBonus, type2 = randomBonus, type4 = randomDamage, type5 = randomDamage),
            SpawnPattern(type2 = randomDamage, type4 = randomDamage, type5 = randomDamage),
            SpawnPattern(type1 = randomDamage, type2 = randomBonus, type4 = randomDamage, type5 = randomDamage)
    )
    private val currentSpawnPattern = GdxArray<ItemType>(spawnPatterns.size)

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        spawnTime -= deltaTime
        if (spawnTime <= 0f) {
            spawnTime = MathUtils.random(MIN_SPAWN_INTERVAL, MAX_SPAWN_INTERVAL)

            if (currentSpawnPattern.isEmpty) {
                currentSpawnPattern.addAll(spawnPatterns[MathUtils.random(0, spawnPatterns.size - 1)].types)
                LOG.debug { "Next pattern: $currentSpawnPattern" }
            }

            val itemType = currentSpawnPattern.removeIndex(0)
            if (itemType == ItemType.None) {
                // nothing to spawn
                return
            }

            spawnItem(itemType, x = 1f * MathUtils.random(0, StarPath.V_WIDTH - 1), y = StarPath.V_HEIGHT.toFloat())
        }
    }

    private fun spawnItem(itemType: ItemType, x: Float, y: Float) {
        engine.entity {
            with<TransformComponent> {
                setInitialPosition(x, y, 0f)
                if (itemType is ItemType.Damage) {
                    val damageSize = MathUtils.random(MIN_DAMAGE_SIZE, MAX_DAMAGE_SIZE)
                    size.set(damageSize, damageSize)
                }
                LOG.debug { "Spawn item of type $itemType at $position" }
            }
            with<ItemComponent> {
                type = itemType
            }

            if (itemType is ItemType.Damage) {
                with<AnimationComponent>() {
                    type = itemType.animationType
                }
                with<GraphicComponent>()
            } else {
                with<GraphicComponent>() {
                    setSpriteRegion(atlas.findRegion(itemType.atlasKey))
                }
            }

            with<MoveComponent> {
                speed.y = ITEM_SPEED
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have a TransformComponent. entity=$entity" }

        val item = entity[ItemComponent.mapper]
        require(item != null) { "Entity |entity| must have a ItemComponent. entity=$entity" }

        if (transform.position.y <= 0f) {
            entity.addComponent<RemoveComponent>(engine)
            return
        }

        val itemCorrelationValue = if (item.type is ItemType.Damage) 0.4f else 0.8f

        playerEntities.forEach { player ->
            player[TransformComponent.mapper]?.let { playerTransform ->
                playerBoundingRect.set(
                        playerTransform.position.x,
                        playerTransform.position.y,
                        playerTransform.size.x * 0.7f,
                        playerTransform.size.y * 0.7f
                )
                itemBoundingRect.set(
                        transform.position.x,
                        transform.position.y,
                        transform.size.x * itemCorrelationValue,
                        transform.size.y * itemCorrelationValue
                )

                if (playerBoundingRect.overlaps(itemBoundingRect)) {
                    collectItem(player, entity)
                }
            }
        }
    }

    private fun collectItem(player: Entity, item: Entity) {
        item[ItemComponent.mapper]?.let { itemCmp ->
            itemCmp.type.also { itemType ->
                LOG.debug { "Picking up item of itemType $itemType" }

                when (itemType) {
                    is ItemType.Boost -> player[PlayerComponent.mapper]?.let { it.points += it.points * itemType.boostValue / 100 }

                    is ItemType.Bonus -> player[PlayerComponent.mapper]?.let {
                        it.points += itemType.bonusPoints
                    }

                    is ItemType.Damage -> {
                        player.addComponent<RemoveComponent>(engine) {
                            delay = 1f
                        }

                        val transform = player[TransformComponent.mapper]
                        require(transform != null) { "Entity |entity| must have a TransformComponent. entity=$player" }

                        player[GraphicComponent.mapper]?.sprite?.setAlpha(0f)
                        engine.entity {
                            with<TransformComponent> {
                                size.set(DEATH_EXPLOSION_SIZE, DEATH_EXPLOSION_SIZE)
                                setInitialPosition(transform.position.x, transform.position.y, 2f)
                            }
                            with<AnimationComponent> {
                                type = AnimationType.EXPLOSION
                            }
                            with<GraphicComponent>()
                            with<RemoveComponent> {
                                delay = DEATH_EXPLOSION_DURATION
                            }
                        }
                    }
                    else -> LOG.error { "Unsupported item of type $itemType" }
                }

                LOG.debug { "Total points: ${player[PlayerComponent.mapper]?.points}" }

                item.addComponent<RemoveComponent>(engine)
            }
        }
    }

    companion object {
        private val LOG = logger<ItemSystem>()
        private const val MAX_SPAWN_INTERVAL = 1.5f
        private const val MIN_SPAWN_INTERVAL = 0.9f
        private const val ITEM_SPEED = -4.5f
        private const val MIN_DAMAGE_SIZE = 2f
        private const val MAX_DAMAGE_SIZE = 5f
        private const val DEATH_EXPLOSION_SIZE = 3f
        private const val DEATH_EXPLOSION_DURATION = 1f
    }

}