package com.mayada1994.starpath

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.mayada1994.starpath.asset.Asset.*
import com.mayada1994.starpath.audio.Audio.AudioService
import com.mayada1994.starpath.audio.Audio.DefaultAudioService
import com.mayada1994.starpath.ecs.system.*
import com.mayada1994.starpath.event.Event
import com.mayada1994.starpath.screens.SplashScreen
import com.mayada1994.starpath.ui.Skin.createSkin
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf

class StarPath : KtxGame<KtxScreen>() {

    val uiViewport: Viewport by lazy {
        FitViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }
    val stage: Stage by lazy {
        val result = Stage(uiViewport, batch)
        Gdx.input.inputProcessor = result
        result
    }
    val gameViewport = FitViewport(V_WIDTH.toFloat(), V_HEIGHT.toFloat())
    val batch: Batch by lazy { SpriteBatch() }
    val gameEventManager = Event.GameEventManager()
    val assets: AssetStorage by lazy {
        KtxAsync.initiate()
        AssetStorage()
    }
    val audioService: AudioService by lazy { DefaultAudioService(assets) }
    val preferences: Preferences by lazy { Gdx.app.getPreferences(APP_PREFERENCES) }

    val engine: Engine by lazy {
        val graphicsAtlas = assets[TextureAtlasAsset.GRAPHICS.descriptor]
        PooledEngine().apply {
            addSystem(PlayerInputSystem(gameViewport))
            addSystem(MoveSystem())
            addSystem(ItemSystem(graphicsAtlas, gameEventManager, audioService))
            addSystem(
                PlayerAnimationSystem(
                    graphicsAtlas.findRegion("player_default"),
                    graphicsAtlas.findRegion("player_left"),
                    graphicsAtlas.findRegion("player_right")
                )
            )
            addSystem(AnimationSystem(graphicsAtlas))
            addSystem(
                RenderSystem(
                    batch,
                    gameViewport,
                    uiViewport,
                    assets[TextureAsset.BACKGROUND.descriptor]
                )
            )
            addSystem(RemoveSystem())
        }
    }

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG

        val assetRefs = gdxArrayOf(
            TextureAtlasAsset.values().filter { it.isSkinAtlas }
                .map { assets.loadAsync(it.descriptor) },
            BitmapFontAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()
        KtxAsync.launch {
            assetRefs.joinAll()
            createSkin(assets)
            addScreen(SplashScreen(this@StarPath))
            setScreen<SplashScreen>()
        }
    }

    override fun dispose() {
        super.dispose()

        batch.dispose()
        assets.dispose()
        stage.dispose()
    }

    companion object {
        const val UNIT_SCALE = 1 / 18f
        const val V_WIDTH = 9
        const val V_HEIGHT = 18
        const val APP_PREFERENCES = "star_path"
        const val PREFERENCES_HIGHSCORE_KEY = "highscore"
    }
}