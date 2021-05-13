package com.mayada1994.starpath.ui

import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import ktx.scene2d.*

class CreditsUI {
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
                    defaults().expandY().fillY()
                    label("Developer", Skin.SkinLabel.GRADIENT.name) {
                        wrap = true
                        setAlignment(Align.top)
                    }
                    row()

                    label("Mayada Al-Sawah\n\n", Skin.SkinLabel.DEFAULT.name) {
                        wrap = true
                        setAlignment(Align.top)
                    }
                    row()

                    label("Composer", Skin.SkinLabel.GRADIENT.name) {
                        wrap = true
                        setAlignment(Align.top)
                    }
                    row()

                    label("Viktor Hahn\n\n", Skin.SkinLabel.DEFAULT.name) {
                        wrap = true
                        setAlignment(Align.top)
                    }
                    row()

                    label("Designers", Skin.SkinLabel.GRADIENT.name) {
                        wrap = true
                        setAlignment(Align.top)
                    }
                    row()

                    label("Jeannie Lee", Skin.SkinLabel.DEFAULT.name) {
                        wrap = true
                        setAlignment(Align.top)
                    }
                    row()

                    label("Viktor Hahn", Skin.SkinLabel.DEFAULT.name) {
                        wrap = true
                        setAlignment(Align.top)
                    }
                    row()

                    label("Code Inferno Games", Skin.SkinLabel.DEFAULT.name) {
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