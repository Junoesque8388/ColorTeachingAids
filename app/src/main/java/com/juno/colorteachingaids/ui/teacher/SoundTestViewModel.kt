package com.juno.colorteachingaids.ui.teacher

import androidx.lifecycle.ViewModel
import com.juno.colorteachingaids.R
import com.juno.colorteachingaids.ui.SoundManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SoundTestViewModel @Inject constructor(
    private val soundManager: SoundManager
) : ViewModel() {

    fun playSound(soundResId: Int) {
        soundManager.playSound(soundResId)
    }
}
