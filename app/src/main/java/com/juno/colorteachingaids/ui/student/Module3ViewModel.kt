package com.juno.colorteachingaids.ui.student

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.R
import com.juno.colorteachingaids.data.local.db.entity.Activity
import com.juno.colorteachingaids.data.repository.ActivityRepository
import com.juno.colorteachingaids.data.repository.ModuleSettingsRepository
import com.juno.colorteachingaids.data.repository.StudentRepository
import com.juno.colorteachingaids.ui.HapticManager
import com.juno.colorteachingaids.ui.SoundManager
import com.juno.colorteachingaids.ui.TTSManager
import com.juno.colorteachingaids.ui.theme.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- DATA & STATE DEFINITIONS ---

data class GameObject(
    val name: String,
    @DrawableRes val greyImageRes: Int,
    @DrawableRes val colorImageRes: Int,
    val colorName: String,
    val color: Color
)

data class Question(
    val correctObject: GameObject,
    val colorChoices: List<ColorChoice>
)

data class ColorChoice(val name: String, val color: Color)

data class Module3State(
    val studentName: String? = null,
    val currentQuestion: Question? = null, // Start as null to indicate loading
    val feedback: Feedback? = null,
    val isGameCompleted: Boolean = false,
    val soundEffectsEnabled: Boolean = false,
    val hapticFeedbackEnabled: Boolean = false,
    val readAloudEnabled: Boolean = false,
    val showColorImage: Boolean = false
)

data class Feedback(val isCorrect: Boolean, val selectedColor: Color)

// --- VIEW MODEL ---

@HiltViewModel
class Module3ViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    private val studentRepository: StudentRepository,
    private val settingsRepository: ModuleSettingsRepository,
    private val soundManager: SoundManager,
    private val hapticManager: HapticManager,
    private val ttsManager: TTSManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(Module3State())
    val uiState: StateFlow<Module3State> = _uiState.asStateFlow()

    private val studentId: Int = savedStateHandle.get<Int>("studentId")!!
    private var startTime = System.currentTimeMillis()
    private var mistakeCount = 0

    private var allObjects = listOf(
        GameObject("Apple", R.drawable.apple_grey, R.drawable.apple_color, "Red", ContentRed),
        GameObject("Strawberry", R.drawable.strawberry_grey, R.drawable.strawberry_color, "Red", ContentRed),
        GameObject("Fire Truck", R.drawable.fire_truck_grey, R.drawable.fire_truck_color, "Red", ContentRed),
        GameObject("Roses", R.drawable.roses_grey, R.drawable.roses_color, "Red", ContentRed),
        GameObject("Sky", R.drawable.sky_grey, R.drawable.sky_color, "Blue", ContentBlue),
        GameObject("Whales", R.drawable.whales_grey, R.drawable.whales_color, "Blue", ContentBlue),
        GameObject("Blue Jeans", R.drawable.blue_jeans_grey, R.drawable.blue_jeans_color, "Blue", ContentBlue),
        GameObject("Banana", R.drawable.banana_grey, R.drawable.banana_color, "Yellow", ContentYellow),
        GameObject("Lemon", R.drawable.lemon_grey, R.drawable.lemon_color, "Yellow", ContentYellow),
        GameObject("Rubber Duck", R.drawable.rubber_duck_grey, R.drawable.rubber_duck_color, "Yellow", ContentYellow),
        GameObject("Leaf", R.drawable.leaf_grey, R.drawable.leaf_color, "Green", ContentGreen),
        GameObject("Frog", R.drawable.frog_grey, R.drawable.frog_color, "Green", ContentGreen),
        GameObject("Cucumber", R.drawable.cucumber_grey, R.drawable.cucumber_color, "Green", ContentGreen),
        GameObject("Orange", R.drawable.orange_grey, R.drawable.orange_color, "Orange", ContentOrange),
        GameObject("Pumpkins", R.drawable.pumpkins_grey, R.drawable.pumpkins_color, "Orange", ContentOrange),
        GameObject("Goldfish", R.drawable.goldfish_grey, R.drawable.goldfish_color, "Orange", ContentOrange),
        GameObject("Grape", R.drawable.grape_grey, R.drawable.grape_color, "Purple", ContentPurple),
        GameObject("Eggplant", R.drawable.eggplant_grey, R.drawable.eggplant_color, "Purple", ContentPurple)
    ).shuffled()

    private var currentQuestionIndex = 0

    init {
        viewModelScope.launch {
            val student = studentRepository.getStudentById(studentId).firstOrNull()
            val initialSettings = settingsRepository.getSettingsForModule(studentId, "Module 3").firstOrNull()
            _uiState.update {
                it.copy(
                    studentName = student?.name,
                    soundEffectsEnabled = initialSettings?.soundEffectsEnabled ?: true,
                    hapticFeedbackEnabled = initialSettings?.hapticFeedbackEnabled ?: true,
                    readAloudEnabled = initialSettings?.readAloudEnabled ?: true
                )
            }

            loadNextQuestion()

            settingsRepository.getSettingsForModule(studentId, "Module 3").drop(1)
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

    fun onColorSelected(selectedChoice: ColorChoice) {
        val currentQuestion = uiState.value.currentQuestion ?: return
        val isCorrect = selectedChoice.name == currentQuestion.correctObject.colorName

        _uiState.update { it.copy(feedback = Feedback(isCorrect, selectedChoice.color)) }

        if (isCorrect) {
            if (uiState.value.soundEffectsEnabled) {
                soundManager.playSound(R.raw.correct_ding)
            }
            if (uiState.value.hapticFeedbackEnabled) {
                hapticManager.performSuccess()
            }
            if (uiState.value.readAloudEnabled) {
                val correctObject = currentQuestion.correctObject
                ttsManager.speak("Yes, correct! The ${correctObject.name.lowercase()} is ${correctObject.colorName.lowercase()}.")
            }
            _uiState.update { it.copy(showColorImage = true) }
        } else {
            mistakeCount++
            if (uiState.value.readAloudEnabled) {
                ttsManager.speak("Try again.")
            }
        }

        viewModelScope.launch {
            delay(3000)
            _uiState.update { it.copy(feedback = null, showColorImage = false) }
            if (isCorrect) {
                loadNextQuestion()
            }
        }
    }

    fun playAgain() {
        mistakeCount = 0
        startTime = System.currentTimeMillis()
        currentQuestionIndex = 0
        allObjects = allObjects.shuffled()
        _uiState.update { it.copy(isGameCompleted = false) }
        loadNextQuestion()
    }

    fun onDone() {
        viewModelScope.launch {
            val duration = System.currentTimeMillis() - startTime
            val activity = Activity(
                studentId = studentId,
                moduleName = "Module 3",
                timestamp = System.currentTimeMillis(),
                duration = duration,
                mistakeCount = mistakeCount
            )
            activityRepository.insertActivity(activity)
        }
    }

    private fun loadNextQuestion() {
        if (currentQuestionIndex >= allObjects.size) {
            _uiState.update { it.copy(isGameCompleted = true, currentQuestion = null) }
            if (uiState.value.readAloudEnabled) {
                ttsManager.speak("Great job! You've finished all the questions.")
            }
            return
        }

        val correctObject = allObjects[currentQuestionIndex]
        val distractors = allObjects
            .asSequence()
            .filter { it.colorName != correctObject.colorName }
            .distinctBy { it.colorName }
            .shuffled()
            .take(3)
            .toList()

        val colorChoices = (distractors.map { ColorChoice(it.colorName, it.color) } + ColorChoice(correctObject.colorName, correctObject.color))
            .shuffled()

        _uiState.update { state ->
            state.copy(
                currentQuestion = Question(correctObject, colorChoices),
                isGameCompleted = false
            )
        }
        if (uiState.value.readAloudEnabled) {
            ttsManager.speak("What color is the ${correctObject.name}?")
        }
        currentQuestionIndex++
    }
}
