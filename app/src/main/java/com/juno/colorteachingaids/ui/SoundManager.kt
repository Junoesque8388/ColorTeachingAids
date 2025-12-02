package com.juno.colorteachingaids.ui

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.juno.colorteachingaids.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A robust SoundManager using SoundPool, designed for low-latency sound effects.
 * It pre-loads all sounds and ensures they are ready before playback is attempted,
 * preventing race conditions.
 */
@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var soundPool: SoundPool
    private val soundMap = mutableMapOf<Int, Int>()
    private val soundsToLoad = listOf(
        R.raw.item_pop,
        R.raw.correct_chime,
        R.raw.color_splash,
        R.raw.clear_whoosh,
        R.raw.correct_ding
    )
    private var soundsLoadedCount = 0

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                soundsLoadedCount++
                if (soundsLoadedCount == soundsToLoad.size) {
                    _isReady.value = true
                }
            }
        }

        soundsToLoad.forEach { soundResId ->
            soundMap[soundResId] = soundPool.load(context, soundResId, 1)
        }
    }

    fun playSound(soundResId: Int) {
        if (!_isReady.value) return // Don't play sounds until all are loaded

        soundMap[soundResId]?.let {
            soundPool.play(it, 1.0f, 1.0f, 0, 0, 1.0f)
        }
    }

    fun release() {
        soundPool.release()
    }
}
