package com.mayada1994.starpath.ui

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.mayada1994.starpath.asset.Asset
import ktx.assets.async.AssetStorage
import ktx.scene2d.Scene2DSkin
import ktx.style.label
import ktx.style.scrollPane
import ktx.style.skin
import ktx.style.textButton

object Skin {

    enum class SkinLabel {
        DEFAULT, GRADIENT
    }

    enum class SkinTextButton {
        DEFAULT
    }

    enum class SkinImage(val atlasKey: String) {
        BUTTON("button")
    }

    enum class SkinScrollPane {
        DEFAULT
    }

    fun createSkin(assets: AssetStorage) {
        val atlas = assets[Asset.TextureAtlasAsset.UI.descriptor]
        val gradientFont = assets[Asset.BitmapFontAsset.FONT_GRADIENT.descriptor]
        val defaultFont = assets[Asset.BitmapFontAsset.FONT_DEFAULT.descriptor]
        Scene2DSkin.defaultSkin = skin(atlas) { skin ->
            skin.createLabelStyles(defaultFont, gradientFont)
            skin.createTextButtonStyles(gradientFont, this)
            skin.createScrollPaneStyles()
        }
    }

    private fun Skin.createTextButtonStyles(
        defaultFont: BitmapFont,
        skin: Skin
    ) {
        textButton(SkinTextButton.DEFAULT.name) {
            font = defaultFont
            up = skin.getDrawable(SkinImage.BUTTON.atlasKey)
            down = up
        }
    }

    private fun Skin.createLabelStyles(
        defaultFont: BitmapFont,
        gradientFont: BitmapFont
    ) {
        label(SkinLabel.DEFAULT.name) {
            font = defaultFont
        }
        label(SkinLabel.GRADIENT.name) {
            font = gradientFont
        }
    }

    private fun Skin.createScrollPaneStyles() {
        scrollPane(SkinScrollPane.DEFAULT.name)
    }

}