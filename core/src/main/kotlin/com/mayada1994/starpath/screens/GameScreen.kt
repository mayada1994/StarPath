package com.mayada1994.starpath.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.StarPath.Companion.UNIT_SCALE
import com.mayada1994.starpath.ecs.component.FacingComponent
import com.mayada1994.starpath.ecs.component.GraphicComponent
import com.mayada1994.starpath.ecs.component.PlayerComponent
import com.mayada1994.starpath.ecs.component.TransformComponent
import ktx.ashley.entity
import ktx.ashley.with

class GameScreen(game: StarPath) : BaseScreen(game) {
    private val playerTexture = Texture(Gdx.files.internal("graphics/player.png"))

    private val player = game.engine.entity {
        with<TransformComponent> {
            position.set(1f, 1f, 0f)
            size.set(2f * 0.9f, 3f * 0.9f)
        }
        with<GraphicComponent> {
            sprite.run {
                setRegion(playerTexture)
                setSize(texture.width * UNIT_SCALE, texture.height * UNIT_SCALE)
                setOriginCenter()
            }
        }
        with<PlayerComponent>()
        with<FacingComponent>()
    }

    override fun render(delta: Float) {
        engine.update(delta)
    }

    override fun dispose() {
        super.dispose()
        playerTexture.dispose()
    }

}