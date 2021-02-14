package com.mayada1994.starpath

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.mayada1994.starpath.screens.MenuScreen
import com.mayada1994.starpath.screens.SplashScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class StarPath : KtxGame<KtxScreen>() {

    val batch: Batch by lazy { SpriteBatch() }

    override fun create() {
        addScreen(SplashScreen(this))
        addScreen(MenuScreen(this))
        setScreen<SplashScreen>()
    }
}