package com.mayada1994.starpath.screens

import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with
import kotlin.math.min

class GameScreen(game: StarPath) : BaseScreen(game) {

    override fun show() {
        super.show()

        game.engine.entity {
            with<TransformComponent> {
                setInitialPosition(1f, 1f, 0f)
            }
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
            with<MoveComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
    }

    companion object {
        private const val MAX_DELTA_TIME = 1 / 20f
    }

}