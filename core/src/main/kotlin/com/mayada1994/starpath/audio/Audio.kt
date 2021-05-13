package com.mayada1994.starpath.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.utils.Pool
import com.mayada1994.starpath.asset.Asset.MusicAsset
import com.mayada1994.starpath.asset.Asset.SoundAsset
import kotlinx.coroutines.launch
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import java.util.*
import kotlin.math.max

class Audio {

    interface AudioService {
        fun play(soundAsset: SoundAsset, volume: Float = 1f)
        fun play(musicAsset: MusicAsset, volume: Float = 1f, loop: Boolean = true)
        fun pause()
        fun resume()
        fun stop(clearSounds: Boolean = true)
        fun update()
    }

    private class SoundRequest : Pool.Poolable {
        lateinit var soundAsset: SoundAsset
        var volume = 1f

        override fun reset() {
            volume = 1f
        }
    }

    private class SoundRequestPool : Pool<SoundRequest>() {
        override fun newObject() = SoundRequest()
    }

    class DefaultAudioService(private val assets: AssetStorage) : AudioService {
        private val soundCache = EnumMap<SoundAsset, Sound>(SoundAsset::class.java)
        private val soundRequestPool = SoundRequestPool()
        private val soundRequests = EnumMap<SoundAsset, SoundRequest>(SoundAsset::class.java)
        private var currentMusic: Music? = null
        private var currentMusicAsset: MusicAsset? = null

        override fun play(soundAsset: SoundAsset, volume: Float) {
            when {
                soundAsset in soundRequests -> {
                    // same request multiple times in one frame -> set volume to maximum of both requests
                    soundRequests[soundAsset]?.let { request ->
                        request.volume = max(request.volume, volume)
                    }
                }

                soundRequests.size >= MAX_SOUND_INSTANCES -> {
                    // maximum simultaneous sound instances reached -> do nothing
                    LOG.debug { "Maximum sound instances reached" }
                    return
                }

                else -> {
                    // new request
                    if (soundAsset.descriptor !in assets) {
                        // sound not loaded -> error
                        LOG.error { "Sound $soundAsset is not loaded" }
                        return
                    } else if (soundAsset !in soundCache) {
                        // cache sound for faster access in the future
                        LOG.debug { "Adding sound $soundAsset to sound cache" }
                        soundCache[soundAsset] = assets[soundAsset.descriptor]
                    }

                    // get request instance from pool and add it to the queue
                    LOG.debug { "New sound request for sound $soundAsset. Free request objects: ${soundRequestPool.free}" }
                    soundRequests[soundAsset] = soundRequestPool.obtain().apply {
                        this.soundAsset = soundAsset
                        this.volume = volume
                    }
                }
            }
        }

        override fun play(musicAsset: MusicAsset, volume: Float, loop: Boolean) {
            val musicDeferred = assets.loadAsync(musicAsset.descriptor)

            KtxAsync.launch {
                musicDeferred.join()
                if (assets.isLoaded(musicAsset.descriptor)) {
                    if (currentMusicAsset == musicAsset) {
                        return@launch
                    }
                    currentMusic?.stop()
                    val currentAsset = currentMusicAsset
                    if (currentAsset != null) {
                        assets.unload(currentAsset.descriptor)
                    }
                    currentMusicAsset = musicAsset
                    currentMusic = assets[musicAsset.descriptor].apply {
                        this.volume = volume
                        this.isLooping = loop
                        play()
                    }
                }
            }
        }

        override fun pause() {
            currentMusic?.pause()
        }

        override fun resume() {
            currentMusic?.play()
        }

        override fun stop(clearSounds: Boolean) {
            currentMusic?.stop()
            if (clearSounds) {
                soundRequests.clear()
            }
        }

        override fun update() {
            if (soundRequests.isNotEmpty()) {
                soundRequests.values.forEach { request ->
                    soundCache[request.soundAsset]?.play(request.volume)
                    soundRequestPool.free(request)
                }
                soundRequests.clear()
            }
        }

    }

    companion object {
        private const val MAX_SOUND_INSTANCES = 16
        private val LOG = logger<Audio>()
    }

}