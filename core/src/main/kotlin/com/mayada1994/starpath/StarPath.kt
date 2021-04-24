package com.mayada1994.starpath

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mayada1994.starpath.ecs.system.RenderSystem
import com.mayada1994.starpath.screens.GameScreen
import com.mayada1994.starpath.screens.MenuScreen
import com.mayada1994.starpath.screens.SplashScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class StarPath : KtxGame<KtxScreen>() {

    val gameViewport = FitViewport(9f, 16f)
    val batch: Batch by lazy { SpriteBatch() }
    val engine: Engine by lazy {
        PooledEngine().apply {
            addSystem(RenderSystem(batch, gameViewport))
        }
    }

    override fun create() {
        addScreen(SplashScreen(this))
        addScreen(MenuScreen(this))
        addScreen(GameScreen(this))
        setScreen<SplashScreen>()
    }

    override fun dispose() {
        super.dispose()

        batch.dispose()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}