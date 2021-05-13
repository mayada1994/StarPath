package com.mayada1994.starpath.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.asset.Asset
import com.mayada1994.starpath.ui.CreditsUI
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.graphics.use

class CreditsScreen(game: StarPath) : BaseScreen(game) {

    private val ui = CreditsUI().apply {
        closeButton.onClick {
            with(game) {
                setScreen<MenuScreen>()
                removeScreen<CreditsScreen>()
            }
        }
    }

    override fun show() {
        super.show()
        setupUI()

    }

    private fun setupUI() {
        ui.run {
            stage += this.table
        }
    }

    override fun render(delta: Float) {
        batch.use {
            Sprite(
                assets[Asset.TextureAsset.BACKGROUND.descriptor]
            ).apply {
                setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
                draw(it)
            }
        }

        stage.run {
            viewport.apply()
            act()
            draw()
        }
    }

    override fun hide() {
        super.hide()
        stage.dispose()
    }

}