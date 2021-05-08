package com.mayada1994.starpath.ui

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import ktx.scene2d.KTableWidget
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton


class MenuUI {
    val table: KTableWidget
    val startGameButton: TextButton
    private val highScoreButton: TextButton
    val creditsButton: TextButton
    val quitGameButton: TextButton

    init {
        table = scene2d.table {
            defaults().expandX().fillX()

            table {
                defaults().expandX().fillX()

                startGameButton = textButton("Start Game", Skin.SkinTextButton.DEFAULT.name)
                row()

                highScoreButton = textButton("Highscore", Skin.SkinTextButton.DEFAULT.name)
                row()

                creditsButton = textButton("Credits", Skin.SkinTextButton.DEFAULT.name)
                row()

                quitGameButton = textButton("Quit Game", Skin.SkinTextButton.DEFAULT.name)
                row()

                center()
                pack()
            }
            row()

            setFillParent(true)
            center()
            pack()
        }
    }

    fun updateHighScore(highScore: Int) {
        highScoreButton.label.run {
            text.setLength(0)
            text.append("Highscore: ${MathUtils.clamp(highScore, 0, MAX_HIGHSCORE_DISPLAYED)}")
            invalidateHierarchy()
        }
    }

    companion object {
        private const val MAX_HIGHSCORE_DISPLAYED = 999
    }
}
