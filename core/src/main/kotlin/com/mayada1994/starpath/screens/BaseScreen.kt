package com.mayada1994.starpath.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import com.mayada1994.starpath.StarPath
import ktx.app.KtxScreen

abstract class BaseScreen(
        val game: StarPath,
        val batch: Batch = game.batch,
        val gameViewport: Viewport = game.gameViewport,
        val engine: Engine = game.engine
) : KtxScreen {

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height, true)
    }

}