package com.juno.colorteachingaids.ui.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.Activity
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.data.repository.ActivityRepository
import com.juno.colorteachingaids.data.repository.ModuleSettingsRepository
import com.juno.colorteachingaids.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// Importing the state class to resolve the build error.
import com.juno.colorteachingaids.ui.teacher.ModuleProgress

@HiltViewModel
class TeacherStudentDetailViewModel @Inject constructor(
    studentRepository: StudentRepository,
    activityRepository: ActivityRepository,
    settingsRepository: ModuleSettingsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val studentId: StateFlow<Int> = savedStateHandle.getStateFlow("studentId", 0)

    val student: StateFlow<Student?> = studentId.flatMapLatest { id ->
        studentRepository.getStudentById(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val recentActivities: StateFlow<List<Activity>> = studentId.flatMapLatest { id ->
        activityRepository.getActivitiesForStudent(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val moduleProgress: StateFlow<List<ModuleProgress>> = studentId.flatMapLatest { id ->
        val activitiesFlow = activityRepository.getActivitiesForStudent(id)
        val settingsFlow = settingsRepository.getAllSettingsForStudent(id)

        activitiesFlow.combine(settingsFlow) { activities, settings ->
            val moduleIds = listOf("Module 1", "Module 2", "Module 3")
            val progressList = moduleIds.map { moduleId ->
                val moduleActivities = activities.filter { it.moduleName == moduleId }.sortedByDescending { it.timestamp }
                val moduleSettings = settings.find { it.moduleId == moduleId }
                val masteryTarget = moduleSettings?.masteryTarget ?: 3

                var consecutiveSuccesses = 0
                for (activity in moduleActivities) {
                    if (activity.mistakeCount == 0) {
                        consecutiveSuccesses++
                    } else {
                        break // Streak is broken
                    }
                }

                val progress = if (masteryTarget > 0) consecutiveSuccesses.toFloat() / masteryTarget.toFloat() else 0f
                ModuleProgress(moduleId, progress.coerceAtMost(1.0f), masteryTarget)
            }
            return@combine progressList // Explicitly return the list to help with type inference
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
