package com.mayada1994.starpath.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.mayada1994.starpath.StarPath
import com.mayada1994.starpath.StarPath.Companion.PREFERENCES_HIGHSCORE_KEY
import com.mayada1994.starpath.asset.Asset.MusicAsset
import com.mayada1994.starpath.ecs.component.*
import com.mayada1994.starpath.event.Event
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

    override fun show() {
        super.show()

        gameEventManager.addListener(Event.GameEvent.PlayerDeath::class, this)

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
    }

    override fun render(delta: Float) {
        engine.update(min(MAX_DELTA_TIME, delta))
        audioService.update()
    }

    override fun hide() {
        super.hide()
        gameEventManager.removeListener(this)
    }

    override fun onEvent(event: Event.GameEvent) {
        if (event is Event.GameEvent.PlayerDeath) {
            preferences.flush {
                if (this[PREFERENCES_HIGHSCORE_KEY, 0] < event.points) {
                    this[PREFERENCES_HIGHSCORE_KEY] = event.points
                }
            }
            logger<GameScreen>().debug {
                "Death with ${event.points} points"
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
    }

    companion object {
        private const val MAX_DELTA_TIME = 1 / 20f
    }

}