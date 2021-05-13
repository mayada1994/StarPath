package com.mayada1994.starpath.ui

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import ktx.scene2d.*

class RulesUI {
    val table: KTableWidget
    val closeButton: TextButton

    init {
        table = scene2d.table {
            defaults().expand().fillX()
            scrollPane(Skin.SkinScrollPane.DEFAULT.name) {
                setScrollbarsVisible(true)
                fadeScrollBars = false
                variableSizeKnobs = false

                table {
                    defaults().expand().fillY().fillX().pad(40f)
                    label("How to play the game\n\n\n", Skin.SkinLabel.GRADIENT.name) {
                        wrap = true
                        setAlignment(Align.top)
                    }
                    row()

                    label(
                        "The player needs to collect bonus and booster items. Each bonus gives the player a different amount of points. " +
                                "The booster increases the gained points by 10%. As time passes the speed of all items will be increased. Beware of asteroids! If the player collides with them the game will be over. " +
                                "Try to gain as many points as possible. Good luck!",
                        Skin.SkinLabel.DEFAULT.name
                    ) {
                        wrap = true
                        setAlignment(Align.top)
                    }
                    row()
                    pack()
                }
            }
            row()

            closeButton = textButton("Back To Menu", Skin.SkinTextButton.DEFAULT.name)
            align(Align.bottom)
            setFillParent(true)
            pack()
        }
    }

}