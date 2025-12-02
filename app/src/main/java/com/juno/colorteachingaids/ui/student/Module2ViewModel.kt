package com.juno.colorteachingaids.ui.student

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.R
import com.juno.colorteachingaids.data.local.db.entity.Activity
import com.juno.colorteachingaids.data.repository.ActivityRepository
import com.juno.colorteachingaids.data.repository.ModuleSettingsRepository
import com.juno.colorteachingaids.ui.ColorUtils
import com.juno.colorteachingaids.ui.HapticManager
import com.juno.colorteachingaids.ui.SoundManager
import com.juno.colorteachingaids.ui.TTSManager
import com.juno.colorteachingaids.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- DATA & STATE DEFINITIONS ---

sealed class MixingMode(val title: String) {
    object Basic : MixingMode("Basic")
    object Tone : MixingMode("Tone")
    object Hue : MixingMode("Hue")
    object Multiple : MixingMode("Multiple")
}

data class PaintColor(val name: String, val color: Color, val type: PaintType)

enum class PaintType { PRIMARY, TONE }

data class MixingState(
    val mode: MixingMode? = null, // Start as null to indicate loading
    val colorsInBowl: List<PaintColor> = emptyList(),
    val bowlColor: Color = Color.LightGray,
    val soundEffectsEnabled: Boolean = false,
    val hapticFeedbackEnabled: Boolean = false,
    val readAloudEnabled: Boolean = false
)

// --- VIEW MODEL ---

@HiltViewModel
class Module2ViewModel @Inject constructor(
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

    private val _uiState = MutableStateFlow(MixingState())
    val uiState: StateFlow<MixingState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val initialSettings = settingsRepository.getSettingsForModule(studentId, "Module 2").firstOrNull()
            _uiState.update {
                it.copy(
                    mode = MixingMode.Basic,
                    soundEffectsEnabled = initialSettings?.soundEffectsEnabled ?: true,
                    hapticFeedbackEnabled = initialSettings?.hapticFeedbackEnabled ?: true,
                    readAloudEnabled = initialSettings?.readAloudEnabled ?: true
                )
            }

            settingsRepository.getSettingsForModule(studentId, "Module 2").drop(1)
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

    val modes = listOf(MixingMode.Basic, MixingMode.Tone, MixingMode.Hue, MixingMode.Multiple)
    private val allPaints = listOf(
        PaintColor("Red", ContentRed, PaintType.PRIMARY),
        PaintColor("Yellow", ContentYellow, PaintType.PRIMARY),
        PaintColor("Blue", ContentBlue, PaintType.PRIMARY),
        PaintColor("White", Color.White, PaintType.TONE),
        PaintColor("Black", Color.Black, PaintType.TONE)
    )

    fun getPaintsForCurrentMode(): List<PaintColor> {
        return when (_uiState.value.mode) {
            MixingMode.Basic, MixingMode.Hue -> allPaints.filter { it.type == PaintType.PRIMARY }
            MixingMode.Tone, MixingMode.Multiple, null -> allPaints
        }
    }

    fun changeMode(newMode: MixingMode) {
        mistakeCount = 0
        startTime = System.currentTimeMillis()
        _uiState.update { it.copy(mode = newMode, colorsInBowl = emptyList(), bowlColor = Color.LightGray) }
        if (uiState.value.readAloudEnabled) {
            ttsManager.speak("Mode changed to ${newMode.title}")
        }
    }

    fun onPaintDropped(paint: PaintColor) {
        val state = _uiState.value
        val newState = when (state.mode) {
            MixingMode.Basic -> handleBasicDrop(state, paint)
            MixingMode.Tone -> handleToneDrop(state, paint)
            MixingMode.Hue -> handleHueDrop(state, paint)
            MixingMode.Multiple -> handleMultipleDrop(state, paint)
            null -> state // Do nothing if mode is not loaded
        }

        if (newState == state) { // If the state didn't change, it was an invalid move
            mistakeCount++
        } else {
            if (state.soundEffectsEnabled) {
                soundManager.playSound(R.raw.color_splash)
            }
            if (state.hapticFeedbackEnabled) {
                hapticManager.performClick()
            }
            if (state.readAloudEnabled) {
                val oldColorName = ColorUtils.findClosestColor(state.bowlColor)
                val droppedColorName = paint.name

                val message = when {
                    oldColorName == "an empty bowl" -> "You get ${ColorUtils.findClosestColor(newState.bowlColor)}."
                    droppedColorName == "White" -> "Adding white to $oldColorName makes it lighter."
                    droppedColorName == "Black" -> "Adding black to $oldColorName makes it darker."
                    paint.type == PaintType.PRIMARY && state.colorsInBowl.any { it.name == paint.name } -> "Adding more $droppedColorName makes it more ${droppedColorName.lowercase()}."
                    else -> {
                        val newColorName = ColorUtils.findClosestColor(newState.bowlColor)
                        "$oldColorName and $droppedColorName make $newColorName."
                    }
                }
                ttsManager.speak(message)
            }
        }
        _uiState.value = newState
    }

    fun clearBowl() {
        if (_uiState.value.soundEffectsEnabled) {
            soundManager.playSound(R.raw.clear_whoosh)
        }
        if (uiState.value.hapticFeedbackEnabled) {
            hapticManager.performClick()
        }
        if (uiState.value.readAloudEnabled) {
            ttsManager.speak("Clearing bowl")
        }
        _uiState.update { it.copy(colorsInBowl = emptyList(), bowlColor = Color.LightGray) }
    }

    fun onDone() {
        viewModelScope.launch {
            val duration = System.currentTimeMillis() - startTime
            val activity = Activity(
                studentId = studentId,
                moduleName = "Module 2",
                timestamp = System.currentTimeMillis(),
                duration = duration,
                mistakeCount = mistakeCount
            )
            activityRepository.insertActivity(activity)
        }
    }

    // --- Game Logic Handlers ---

    private fun handleBasicDrop(state: MixingState, paint: PaintColor): MixingState {
        if (paint.type == PaintType.TONE || state.colorsInBowl.size >= 2 || state.colorsInBowl.any { it.name == paint.name }) {
            return state
        }
        val newColors = state.colorsInBowl + paint
        return state.copy(colorsInBowl = newColors, bowlColor = calculateColor(newColors))
    }

    private fun handleToneDrop(state: MixingState, paint: PaintColor): MixingState {
        val newColors = state.colorsInBowl + paint
        return state.copy(colorsInBowl = newColors, bowlColor = calculateColor(newColors))
    }

    private fun handleHueDrop(state: MixingState, paint: PaintColor): MixingState {
        if (paint.type == PaintType.TONE) return state
        val newColors = state.colorsInBowl + paint
        return state.copy(colorsInBowl = newColors, bowlColor = calculateColor(newColors))
    }

    private fun handleMultipleDrop(state: MixingState, paint: PaintColor): MixingState {
        val newColors = state.colorsInBowl + paint
        return state.copy(colorsInBowl = newColors, bowlColor = calculateColor(newColors))
    }

    private fun calculateColor(paints: List<PaintColor>): Color {
        if (paints.isEmpty()) return Color.LightGray

        val primaryPaints = paints.filter { it.type == PaintType.PRIMARY }
        val uniquePrimaryNames = primaryPaints.map { it.name }.toSet()
        var baseColor: Color

        baseColor = when {
            uniquePrimaryNames.containsAll(listOf("Red", "Yellow", "Blue")) -> Color.Gray
            uniquePrimaryNames.containsAll(listOf("Red", "Yellow")) -> ContentOrange
            uniquePrimaryNames.containsAll(listOf("Red", "Blue")) -> ContentPurple
            uniquePrimaryNames.containsAll(listOf("Blue", "Yellow")) -> ContentGreen
            uniquePrimaryNames.size == 1 -> primaryPaints.first().color
            else -> Color.LightGray
        }

        if ((_uiState.value.mode == MixingMode.Hue || _uiState.value.mode == MixingMode.Multiple) && primaryPaints.size > uniquePrimaryNames.size) {
            val excessPaints = primaryPaints.toMutableList()
            uniquePrimaryNames.forEach { name -> excessPaints.remove(excessPaints.first { it.name == name }) }
            
            excessPaints.forEach { paint ->
                baseColor = lerp(baseColor, paint.color, 0.33f)
            }
        }

        val tonePaints = paints.filter { it.type == PaintType.TONE }
        var finalColor = baseColor
        tonePaints.forEach { tonePaint ->
            val targetColor = if (tonePaint.name == "White") Color.White else Color.Black
            finalColor = lerp(finalColor, targetColor, 0.2f)
        }

        return finalColor
    }
}
