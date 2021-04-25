package com.mayada1994.starpath.screens

import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.ecs.component.FacingComponent
import com.mayada1994.starpath.ecs.component.GraphicComponent
import com.mayada1994.starpath.ecs.component.PlayerComponent
import com.mayada1994.starpath.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with

class GameScreen(game: StarPath) : BaseScreen(game) {

    override fun show() {
        super.show()

        game.engine.entity {
            with<TransformComponent> {
                position.set(1f, 0f, 0f)
            }
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
        }
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

}