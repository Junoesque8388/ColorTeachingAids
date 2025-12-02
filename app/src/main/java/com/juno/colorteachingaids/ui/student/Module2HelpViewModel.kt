package com.juno.colorteachingaids.ui.student

import androidx.lifecycle.ViewModel
import com.juno.colorteachingaids.ui.TTSManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Module2HelpViewModel @Inject constructor(
    private val ttsManager: TTSManager
) : ViewModel() {

    fun onReadAloud(text: String) {
        ttsManager.speak(text)
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown() // Properly release resources
    }
}
