package com.mayada1994.starpath.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.asset.Asset
import com.mayada1994.starpath.ecs.system.ItemSystem
import com.mayada1994.starpath.ecs.system.MoveSystem
import com.mayada1994.starpath.ecs.system.PlayerAnimationSystem
import com.mayada1994.starpath.ui.MenuUI
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.ashley.getSystem
import ktx.graphics.use

class MenuScreen(game: StarPath, private val engine: Engine = game.engine) : BaseScreen(game) {

    private val ui = MenuUI().apply {
        startGameButton.onClick {
            with(game) {
                addScreen(GameScreen(this))
                setScreen<GameScreen>()
                removeScreen<MenuScreen>()
            }
            dispose()
        }
        rulesButton.onClick {

        }
        creditsButton.onClick {
            with(game) {
                addScreen(CreditsScreen(this))
                setScreen<CreditsScreen>()
            }
        }
        quitGameButton.onClick {
            Gdx.app.exit()
        }
    }

    override fun show() {
        super.show()

        audioService.play(Asset.MusicAsset.MENU)
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

        engine.run {
            getSystem<ItemSystem>().setProcessing(true)
            getSystem<MoveSystem>().setProcessing(true)
            getSystem<PlayerAnimationSystem>().setProcessing(true)
            removeAllEntities()
        }
    }

}