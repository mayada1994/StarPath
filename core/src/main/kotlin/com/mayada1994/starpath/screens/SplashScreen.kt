package com.mayada1994.starpath.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.mayada1994.starpath.StarPath
import ktx.graphics.use


class SplashScreen(game: StarPath) : BaseScreen(game) {

    override fun render(delta: Float) {
        batch.use {
            Sprite(
                    Texture(Gdx.files.internal("graphics/splash_screen.png"))
            ).apply {
                setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
                draw(it)
            }
        }

        game.setScreen<MenuScreen>()
    }

    override fun resize(width: Int, height: Int) = Unit

}