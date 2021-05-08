package com.mayada1994.starpath.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.asset.Asset.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.collections.gdxArrayOf
import ktx.graphics.use


class SplashScreen(game: StarPath) : BaseScreen(game) {

    override fun show() {
        //queue asset loading
        val assetRefs = gdxArrayOf(
            TextureAsset.values().map { assets.loadAsync(it.descriptor) },
            TextureAtlasAsset.values().map { assets.loadAsync(it.descriptor) },
            SoundAsset.values().map { assets.loadAsync(it.descriptor) },
            MusicAsset.values().map { assets.loadAsync(it.descriptor) }
        ).flatten()

        //change to GameScreen on assets loading finished
        KtxAsync.launch {
            assetRefs.joinAll()
            assetsLoaded()
        }
    }

    private fun assetsLoaded() {
        with(game) {
            addScreen(MenuScreen(this))
            setScreen<MenuScreen>()
            removeScreen<SplashScreen>()
        }
        dispose()
    }

    override fun render(delta: Float) {
        batch.use {
            Sprite(
                Texture(Gdx.files.internal("graphics/splash_screen.png"))
            ).apply {
                setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
                draw(it)
            }
        }
    }

    override fun resize(width: Int, height: Int) = Unit

}