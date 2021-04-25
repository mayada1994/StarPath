package com.mayada1994.starpath.screens

import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with

class GameScreen(game: StarPath) : BaseScreen(game) {

    override fun show() {
        super.show()

        game.engine.entity {
            with<TransformComponent> {
                position.set(1f, 1f, 0f)
            }
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
            with<MoveComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

}