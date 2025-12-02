package com.juno.colorteachingaids.ui.student

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.R
import com.juno.colorteachingaids.data.local.db.entity.Activity
import com.juno.colorteachingaids.data.repository.ActivityRepository
import com.juno.colorteachingaids.data.repository.ModuleSettingsRepository
import com.juno.colorteachingaids.ui.HapticManager
import com.juno.colorteachingaids.ui.SoundManager
import com.juno.colorteachingaids.ui.TTSManager
import com.juno.colorteachingaids.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Module1ViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    private val settingsRepository: ModuleSettingsRepository,
    private val soundManager: SoundManager,
    private val hapticManager: HapticManager,
    private val ttsManager: TTSManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val studentId: Int = savedStateHandle.get<Int>("studentId")!!
    private var startTime = System.currentTimeMillis()
    private var mistakeCount = 0
    private val dropTargets = mutableMapOf<ColorInfo, Rect>()

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val initialSettings = settingsRepository.getSettingsForModule(studentId, "Module 1").firstOrNull()
            _uiState.update {
                createInitialGameState().copy(
                    soundEffectsEnabled = initialSettings?.soundEffectsEnabled ?: true,
                    hapticFeedbackEnabled = initialSettings?.hapticFeedbackEnabled ?: true,
                    readAloudEnabled = initialSettings?.readAloudEnabled ?: true // Default to true
                )
            }

            settingsRepository.getSettingsForModule(studentId, "Module 1").drop(1)
                .onEach { settings ->
                    _uiState.update {
                        it.copy(
                            soundEffectsEnabled = settings?.soundEffectsEnabled ?: true,
                            hapticFeedbackEnabled = settings?.hapticFeedbackEnabled ?: true,
                            readAloudEnabled = settings?.readAloudEnabled ?: true
                        )
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    fun onDragStart(item: ColorInfo, startPosition: Offset) {
        _uiState.update {
            it.copy(
                isDragging = true,
                draggedItem = item,
                dragPosition = startPosition
            )
        }
        if (uiState.value.soundEffectsEnabled) {
            soundManager.playSound(R.raw.item_pop)
        }
        if (uiState.value.hapticFeedbackEnabled) {
            hapticManager.performClick()
        }
        if (uiState.value.readAloudEnabled) {
            ttsManager.speak(item.name) // Speak color name on drag
        }
    }

    fun onDrag(dragAmount: Offset) {
        _uiState.update { it.copy(dragPosition = it.dragPosition + dragAmount) }
    }

    fun onDrop() {
        val dragged = _uiState.value.draggedItem ?: return
        val finalPosition = _uiState.value.dragPosition
        val target = dropTargets.entries.find { (_, bounds) -> bounds.contains(finalPosition) }?.key

        val successful = target?.name == dragged.name

        if (successful) {
            if (uiState.value.soundEffectsEnabled) {
                soundManager.playSound(R.raw.correct_chime)
            }
            if (uiState.value.hapticFeedbackEnabled) {
                hapticManager.performSuccess()
            }
            if (uiState.value.readAloudEnabled) {
                ttsManager.speak("Matched!") // Speak confirmation on correct drop
            }
            _uiState.update {
                it.copy(
                    matchedPairs = it.matchedPairs + (dragged.name to true),
                    isDragging = false,
                    draggedItem = null
                )
            }
        } else {
            mistakeCount++
            _uiState.update { it.copy(isDragging = false, draggedItem = null) }
        }
    }

    fun updateDropTarget(item: ColorInfo, bounds: Rect) {
        dropTargets[item] = bounds
    }

    fun playAgain() {
        mistakeCount = 0
        startTime = System.currentTimeMillis()
        _uiState.value = createInitialGameState().copy(
            soundEffectsEnabled = _uiState.value.soundEffectsEnabled,
            hapticFeedbackEnabled = _uiState.value.hapticFeedbackEnabled,
            readAloudEnabled = _uiState.value.readAloudEnabled
        )
    }

    fun onDone() {
        viewModelScope.launch {
            val duration = System.currentTimeMillis() - startTime
            val activity = Activity(
                studentId = studentId,
                moduleName = "Module 1",
                timestamp = System.currentTimeMillis(),
                duration = duration,
                mistakeCount = mistakeCount
            )
            activityRepository.insertActivity(activity)
        }
    }

    private fun createInitialGameState(): GameState {
        val colors = listOf(
            ColorInfo("Red", ContentRed),
            ColorInfo("Green", ContentGreen),
            ColorInfo("Blue", ContentBlue),
            ColorInfo("Yellow", ContentYellow),
            ColorInfo("Purple", ContentPurple),
            ColorInfo("Orange", ContentOrange)
        ).shuffled()

        val matched = colors.associate { it.name to false }

        return GameState(
            colorItems = colors.shuffled(),
            colorTargets = colors.shuffled(),
            matchedPairs = matched
        )
    }
}
