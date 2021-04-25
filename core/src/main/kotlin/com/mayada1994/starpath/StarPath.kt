package com.mayada1994.starpath

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mayada1994.starpath.ecs.system.*
import com.mayada1994.starpath.screens.GameScreen
import com.mayada1994.starpath.screens.MenuScreen
import com.mayada1994.starpath.screens.SplashScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class StarPath : KtxGame<KtxScreen>() {

    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val batch: Batch by lazy { SpriteBatch() }

    private val graphicsAtlas by lazy { TextureAtlas(Gdx.files.internal("graphics/graphics.atlas")) }

    val engine: Engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(PlayerAnimationSystem(
                    graphicsAtlas.findRegion("player_default"),
                    graphicsAtlas.findRegion("player_left"),
                    graphicsAtlas.findRegion("player_right")
            ))
            addSystem(RenderSystem(batch, gameViewport))
            addSystem(RemoveSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG

        addScreen(SplashScreen(this))
        addScreen(MenuScreen(this))
        addScreen(GameScreen(this))
        setScreen<SplashScreen>()
    }

    override fun dispose() {
        super.dispose()

        batch.dispose()

        graphicsAtlas.dispose()
    }

    companion object {
        const val UNIT_SCALE = 1 / 6f
        const val V_WIDTH = 3
        const val V_HEIGHT = 6
    }
}