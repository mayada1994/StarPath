package com.mayada1994.starpath.screens

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.audio.Audio.AudioService
import com.mayada1994.starpath.event.Event
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage

abstract class BaseScreen(
        val game: StarPath,
        val batch: Batch = game.batch,
        val gameViewport: Viewport = game.gameViewport,
        val uiViewport: Viewport = game.uiViewport,
        val gameEventManager: Event.GameEventManager = game.gameEventManager,
        val assets: AssetStorage = game.assets,
        val audioService: AudioService = game.audioService
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

}