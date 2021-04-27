package com.mayada1994.starpath.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.mayada1994.starpath.StarPath.Companion.V_HEIGHT
import com.mayada1994.starpath.StarPath.Companion.V_WIDTH
import com.mayada1994.starpath.ecs.component.*
import com.mayada1994.starpath.ecs.component.BonusComponent.BonusType
import ktx.ashley.*
import ktx.collections.GdxArray
import ktx.collections.gdxArrayOf
import ktx.log.debug
import ktx.log.error
import ktx.log.logger

class BonusSystem : IteratingSystem(allOf(BonusComponent::class, TransformComponent::class).exclude(RemoveComponent::class).get()) {

    private class SpawnPattern(
            type1: BonusType = BonusType.NONE,
            type2: BonusType = BonusType.NONE,
            type3: BonusType = BonusType.NONE,
            type4: BonusType = BonusType.NONE,
            type5: BonusType = BonusType.NONE,
            val types: GdxArray<BonusType> = gdxArrayOf(type1, type2, type3, type4, type5)
    )

    private val playerBoundingRect = Rectangle()
    private val bonusBoundingRect = Rectangle()
    private val playerEntities by lazy {
        engine.getEntitiesFor(
                allOf(PlayerComponent::class).exclude(RemoveComponent::class).get()
        )
    }
    private var spawnTime = 0f
    private val spawnPatterns = gdxArrayOf(
            SpawnPattern(type1 = BonusType.STAR_1, type2 = BonusType.STAR_2, type5 = BonusType.STAR_1),
            SpawnPattern(type1 = BonusType.STAR_3, type2 = BonusType.STAR_1, type5 = BonusType.STAR_3),
            SpawnPattern(type2 = BonusType.BOOST, type4 = BonusType.STAR_1, type5 = BonusType.STAR_1),
            SpawnPattern(type2 = BonusType.STAR_2, type4 = BonusType.STAR_2),
            SpawnPattern(type2 = BonusType.STAR_2, type5 = BonusType.STAR_3),
            SpawnPattern(type1 = BonusType.STAR_3, type4 = BonusType.STAR_3),
            SpawnPattern(type2 = BonusType.STAR_2, type3 = BonusType.STAR_1),
            SpawnPattern(type1 = BonusType.STAR_3, type2 = BonusType.STAR_1, type4 = BonusType.STAR_2, type5 = BonusType.STAR_1),
            SpawnPattern(type2 = BonusType.STAR_1, type4 = BonusType.STAR_2, type5 = BonusType.STAR_1),
            SpawnPattern(type1 = BonusType.STAR_2, type2 = BonusType.STAR_1, type4 = BonusType.STAR_2, type5 = BonusType.STAR_1)
    )
    private val currentSpawnPattern = GdxArray<BonusType>(spawnPatterns.size)

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        spawnTime -= deltaTime
        if (spawnTime <= 0f) {
            spawnTime = MathUtils.random(MIN_SPAWN_INTERVAL, MAX_SPAWN_INTERVAL)

            if (currentSpawnPattern.isEmpty) {
                currentSpawnPattern.addAll(spawnPatterns[MathUtils.random(0, spawnPatterns.size - 1)].types)
                LOG.debug { "Next pattern: $currentSpawnPattern" }
            }

            val bonusType = currentSpawnPattern.removeIndex(0)
            if (bonusType == BonusType.NONE) {
                // nothing to spawn
                return
            }

            spawnBonus(bonusType, x = 1f * MathUtils.random(0, V_WIDTH - 1), y = 16f)
        }
    }

    private fun spawnBonus(bonusType: BonusType, x: Float, y: Float) {
        engine.entity {
            with<TransformComponent> {
                setInitialPosition(x, V_HEIGHT.toFloat(), 0f)
                LOG.debug { "Spawn bonus of type $bonusType at $position" }
            }
            with<BonusComponent> {
                type = bonusType
            }
            with<GraphicComponent>() {
                setSpriteRegion(TextureRegion(Texture(bonusType.atlasKey)))
            }
            with<MoveComponent> {
                speed.y = BONUS_SPEED
            }
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have a TransformComponent. entity=$entity" }

        if (transform.position.y <= 0f) {
            entity.addComponent<RemoveComponent>(engine)
            return
        }

        playerEntities.forEach { player ->
            player[TransformComponent.mapper]?.let { playerTransform ->
                playerBoundingRect.set(
                        playerTransform.position.x,
                        playerTransform.position.y,
                        playerTransform.size.x * 0.7f,
                        playerTransform.size.y
                )
                bonusBoundingRect.set(
                        transform.position.x,
                        transform.position.y,
                        transform.size.x,
                        transform.size.y
                )

                if (playerBoundingRect.overlaps(bonusBoundingRect)) {
                    collectBonus(player, entity)
                }
            }
        }
    }

    private fun collectBonus(player: Entity, bonus: Entity) {
        bonus[BonusComponent.mapper]?.let { bonusCmp ->
            bonusCmp.type.also { bonusType ->
                LOG.debug { "Picking up bonus of bonusType $bonusType" }

                when (bonusType) {
                    BonusType.BOOST -> player[MoveComponent.mapper]?.let { it.speed.y *= 0.5f }

                    BonusType.STAR_1 -> player[PlayerComponent.mapper]?.let {
                        it.points += STAR_1_POINT_GAIN
                    }

                    BonusType.STAR_2 -> player[PlayerComponent.mapper]?.let {
                        it.points += STAR_2_POINT_GAIN
                    }

                    BonusType.STAR_3 -> player[PlayerComponent.mapper]?.let {
                        it.points += STAR_3_POINT_GAIN
                    }

                    else -> LOG.error { "Unsupported bonus of type $bonusType" }
                }
                bonus.addComponent<RemoveComponent>(engine)
            }
        }
    }

    companion object {
        private val LOG = logger<BonusSystem>()
        private const val MAX_SPAWN_INTERVAL = 1.5f
        private const val MIN_SPAWN_INTERVAL = 0.9f
        private const val BONUS_SPEED = -4.5f
        private const val STAR_1_POINT_GAIN = 5
        private const val STAR_2_POINT_GAIN = 10
        private const val STAR_3_POINT_GAIN = 25
    }

}