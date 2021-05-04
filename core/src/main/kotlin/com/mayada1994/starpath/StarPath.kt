package com.mayada1994.starpath

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mayada1994.starpath.ecs.system.*
import com.mayada1994.starpath.event.Event
import com.mayada1994.starpath.screens.GameScreen
import com.mayada1994.starpath.screens.MenuScreen
import com.mayada1994.starpath.screens.SplashScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class StarPath : KtxGame<KtxScreen>() {

    val uiViewport = FitViewport(V_WIDTH_PIXELS.toFloat(), V_HEIGHT_PIXELS.toFloat())
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val batch: Batch by lazy { SpriteBatch() }

    val gameEventManager = Event.GameEventManager()

    private val graphicsAtlas by lazy { TextureAtlas(Gdx.files.internal("graphics/graphics.atlas")) }
    private val backgroundTexture by lazy { Texture("graphics/background.png") }

    val engine: Engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(ItemSystem(graphicsAtlas, gameEventManager))
            addSystem(PlayerAnimationSystem(
                    graphicsAtlas.findRegion("player_default"),
                    graphicsAtlas.findRegion("player_left"),
                    graphicsAtlas.findRegion("player_right")
            ))
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(RenderSystem(batch, gameViewport, uiViewport, backgroundTexture))
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
        backgroundTexture.dispose()
    }

    companion object {
        const val UNIT_SCALE = 1 / 18f
        const val V_WIDTH = 9
        const val V_HEIGHT = 18
        const val V_WIDTH_PIXELS = 1080
        const val V_HEIGHT_PIXELS = 2260
    }
}