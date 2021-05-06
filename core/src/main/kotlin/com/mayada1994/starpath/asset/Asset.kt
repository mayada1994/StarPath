package com.mayada1994.starpath.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class Asset {

    enum class TextureAsset(
        fileName: String,
        directory: String = "graphics",
        val descriptor: AssetDescriptor<Texture> = AssetDescriptor(
            "$directory/$fileName",
            Texture::class.java
        )
    ) {
        BACKGROUND("background.png")
    }

    enum class TextureAtlasAsset(
        fileName: String,
        directory: String = "graphics",
        val descriptor: AssetDescriptor<TextureAtlas> = AssetDescriptor(
            "$directory/$fileName",
            TextureAtlas::class.java
        )
    ) {
        GRAPHICS("graphics.atlas")
    }

    enum class SoundAsset(
        fileName: String,
        directory: String = "sound",
        val descriptor: AssetDescriptor<Sound> = AssetDescriptor(
            "$directory/$fileName",
            Sound::class.java
        )
    ) {
        BONUS("bonus.wav"),
        BOOST("boost.wav"),
        DAMAGE("damage.wav")
    }

    enum class MusicAsset(
        fileName: String,
        directory: String = "music",
        val descriptor: AssetDescriptor<Music> = AssetDescriptor(
            "$directory/$fileName",
            Music::class.java
        )
    ) {
        MENU("menu.ogg"),
        GAME("game.ogg"),
        GAME_OVER("gameOver.ogg")
    }

}