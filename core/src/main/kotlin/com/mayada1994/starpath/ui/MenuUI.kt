package com.mayada1994.starpath.ui

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import ktx.scene2d.KTableWidget
import ktx.scene2d.scene2d
import ktx.scene2d.table
import ktx.scene2d.textButton


class MenuUI {
    val table: KTableWidget
    val startGameButton: TextButton
    val rulesButton: TextButton
    val creditsButton: TextButton
    val quitGameButton: TextButton

    init {
        table = scene2d.table {
            defaults().expandX().fillX()

            table {
                defaults().expandX().fillX()

                startGameButton = textButton("Start Game", Skin.SkinTextButton.DEFAULT.name)
                row()

                rulesButton = textButton("Rules", Skin.SkinTextButton.DEFAULT.name)
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

}
