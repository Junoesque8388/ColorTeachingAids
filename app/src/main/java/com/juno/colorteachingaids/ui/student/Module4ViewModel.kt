package com.juno.colorteachingaids.ui.student

import androidx.lifecycle.ViewModel
import com.juno.colorteachingaids.ui.TTSManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class ColorExplorerState(
    val detectedColorName: String = ""
)

@HiltViewModel
class Module4ViewModel @Inject constructor(
    private val ttsManager: TTSManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ColorExplorerState())
    val uiState: StateFlow<ColorExplorerState> = _uiState

    fun onColorDetected(colorName: String) {
        if (_uiState.value.detectedColorName != colorName) {
            _uiState.value = _uiState.value.copy(detectedColorName = colorName)
            ttsManager.speak(colorName)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Consider if TTS should be shut down here or if it's managed as a singleton.
    }
}
