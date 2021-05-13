package com.mayada1994.starpath.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.StarPath.Companion.PREFERENCES_HIGHSCORE_KEY
import com.mayada1994.starpath.asset.Asset.MusicAsset
import com.mayada1994.starpath.ecs.component.*
import com.mayada1994.starpath.event.Event
import com.mayada1994.starpath.ui.GameUI
import ktx.actors.plusAssign
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktx.preferences.flush
import ktx.preferences.get
import ktx.preferences.set
import kotlin.math.min

class GameScreen(game: StarPath, private val engine: Engine = game.engine) : BaseScreen(game),
    Event.GameEventListener {

    private val ui = GameUI()

    override fun show() {
        super.show()

        gameEventManager.addListener(Event.GameEvent.PlayerDeath::class, this)
        gameEventManager.addListener(Event.GameEvent.CollectBonus::class, this)

        audioService.play(MusicAsset.GAME)

        game.engine.entity {
            with<TransformComponent> {
                setInitialPosition(1f, 1f, 0f)
            }
            with<GraphicComponent>()
            with<PlayerComponent>()
            with<FacingComponent>()
            with<MoveComponent>()
        }

        ui.run {
            stage += this.table
            updateHighScore(preferences[PREFERENCES_HIGHSCORE_KEY, 0])
        }
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
        audioService.update()

        stage.run {
            viewport.apply()
            act()
            draw()
        }
    }

    override fun hide() {
        super.hide()
        stage.dispose()
        gameEventManager.removeListener(this)
    }

    override fun onEvent(event: Event.GameEvent) {
        when (event) {
            is Event.GameEvent.PlayerDeath -> {
                preferences.flush {
                    if (this[PREFERENCES_HIGHSCORE_KEY, 0] < event.points) {
                        this[PREFERENCES_HIGHSCORE_KEY] = event.points
                    }
                }
                Thread {
                    val time = System.currentTimeMillis()
                    while (System.currentTimeMillis() < time + 1700) {
                        logger<GameScreen>().debug {
                            "Waiting for animation end"
                        }
                    }
                    Gdx.app.postRunnable {
                        with(game) {
                            addScreen(MenuScreen(this))
                            setScreen<MenuScreen>()
                            removeScreen<GameScreen>()
                        }
                        dispose()
                    }
                }.start()
            }

            is Event.GameEvent.CollectBonus -> ui.updateCurrentScore(event.points)
        }
    }

    companion object {
        private const val MAX_DELTA_TIME = 1 / 20f
    }

}