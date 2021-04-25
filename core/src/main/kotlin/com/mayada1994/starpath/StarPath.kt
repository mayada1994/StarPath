package com.mayada1994.starpath

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.FitViewport
import com.mayada1994.starpath.ecs.system.PlayerAnimationSystem
import com.mayada1994.starpath.ecs.system.PlayerInputSystem
import com.mayada1994.starpath.ecs.system.RenderSystem
import com.mayada1994.starpath.screens.GameScreen
import com.mayada1994.starpath.screens.MenuScreen
import com.mayada1994.starpath.screens.SplashScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class StarPath : KtxGame<KtxScreen>() {

    val gameViewport = FitViewport(3f, 5f)
    val batch: Batch by lazy { SpriteBatch() }

    private val defaultRegion by lazy { TextureRegion(Texture(Gdx.files.internal("graphics/player_default.png"))) }
    private val leftRegion by lazy { TextureRegion(Texture(Gdx.files.internal("graphics/player_left.png"))) }
    private val rightRegion by lazy { TextureRegion(Texture(Gdx.files.internal("graphics/player_right.png"))) }

    val engine: Engine by lazy {
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(PlayerAnimationSystem(
                    defaultRegion,
                    leftRegion,
                    rightRegion
            ))
            addSystem(RenderSystem(batch, gameViewport))
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

        defaultRegion.texture.dispose()
        leftRegion.texture.dispose()
        rightRegion.texture.dispose()
    }

    companion object {
        const val UNIT_SCALE = 1 / 5f
    }
}