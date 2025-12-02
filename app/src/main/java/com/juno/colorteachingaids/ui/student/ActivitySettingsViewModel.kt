package com.juno.colorteachingaids.ui.student

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.ModuleSettings
import com.juno.colorteachingaids.data.repository.ModuleSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActivitySettingsUiState(
    val soundEffectsEnabled: Boolean = false,
    val hapticFeedbackEnabled: Boolean = false,
    val readAloudEnabled: Boolean = false,
    val masteryTarget: Int = 3
)

@HiltViewModel
class ActivitySettingsViewModel @Inject constructor(
    private val settingsRepository: ModuleSettingsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val studentId: Int = savedStateHandle.get<Int>("studentId")!!
    private val moduleId: String = savedStateHandle.get<String>("moduleId")!!

    private val _uiState = MutableStateFlow(ActivitySettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        settingsRepository.getSettingsForModule(studentId, moduleId)
            .onEach { settings ->
                _uiState.update {
                    it.copy(
                        soundEffectsEnabled = settings?.soundEffectsEnabled ?: true,
                        hapticFeedbackEnabled = settings?.hapticFeedbackEnabled ?: true,
                        readAloudEnabled = settings?.readAloudEnabled ?: true,
                        masteryTarget = settings?.masteryTarget ?: 3
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSoundEffectsChanged(enabled: Boolean) {
        _uiState.update { it.copy(soundEffectsEnabled = enabled) }
    }

    fun onHapticFeedbackChanged(enabled: Boolean) {
        _uiState.update { it.copy(hapticFeedbackEnabled = enabled) }
    }

    fun onReadAloudChanged(enabled: Boolean) {
        _uiState.update { it.copy(readAloudEnabled = enabled) }
    }

    fun onMasteryTargetChanged(target: String) {
        val intValue = target.toIntOrNull() ?: 0
        _uiState.update { it.copy(masteryTarget = intValue) }
    }

    fun saveSettings() {
        viewModelScope.launch {
            val currentSettings = _uiState.value
            val settings = ModuleSettings(
                studentId = studentId,
                moduleId = moduleId,
                soundEffectsEnabled = currentSettings.soundEffectsEnabled,
                hapticFeedbackEnabled = currentSettings.hapticFeedbackEnabled,
                readAloudEnabled = currentSettings.readAloudEnabled,
                masteryTarget = currentSettings.masteryTarget
            )
            settingsRepository.insertOrUpdateSettings(settings)
        }
    }
}
