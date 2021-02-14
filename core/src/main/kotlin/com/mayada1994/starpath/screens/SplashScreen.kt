package com.mayada1994.starpath.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.mayada1994.starpath.StarPath
import java.util.*
import java.util.concurrent.TimeUnit


class SplashScreen(game: StarPath) : BaseScreen(game) {

    override fun render(delta: Float) {
        batch.begin()
        Sprite(
                Texture(Gdx.files.internal("graphics/splash_screen.png"))
        ).apply {
            setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
            draw(batch)
        }
        batch.end()

        Timer().schedule(object: TimerTask(){
            override fun run() {
                game.setScreen<MenuScreen>()
            }
        }, TimeUnit.SECONDS.toMillis(5))
    }

}