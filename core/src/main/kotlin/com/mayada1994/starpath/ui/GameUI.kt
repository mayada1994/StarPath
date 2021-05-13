package com.mayada1994.starpath.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import ktx.scene2d.KTableWidget
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.table

class GameUI {
    val table: KTableWidget
    private val highScoreLabel: Label
    private val currentScoreLabel: Label

    init {
        table = scene2d.table {
            defaults().expandX().padTop(20f)

            highScoreLabel = label("Highscore: 0", Skin.SkinLabel.DEFAULT.name)

            currentScoreLabel = label("Current score: 0", Skin.SkinLabel.DEFAULT.name)

            setFillParent(true)
            align(Align.top)
            pack()
        }
    }

    fun updateHighScore(highScore: Int) {
        highScoreLabel.run {
            text.setLength(0)
            text.append("Highscore: $highScore")
            invalidateHierarchy()
        }
    }

    fun updateCurrentScore(score: Int) {
        currentScoreLabel.run {
            text.setLength(0)
            text.append("Current score: $score")
            invalidateHierarchy()
        }
    }

}