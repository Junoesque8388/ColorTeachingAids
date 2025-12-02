package com.juno.colorteachingaids.ui.student

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    fun onSoundEffectsChanged(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(soundEffectsEnabled = enabled)
    }

    fun onHapticFeedbackChanged(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(hapticFeedbackEnabled = enabled)
    }

    fun onAnimationSpeedChanged(speed: Float) {
        _uiState.value = _uiState.value.copy(animationSpeed = speed)
    }
}

data class SettingsUiState(
    val soundEffectsEnabled: Boolean = true,
    val hapticFeedbackEnabled: Boolean = true,
    val animationSpeed: Float = 1f // 0.5f, 1f, 1.5f
)
