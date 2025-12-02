package com.juno.colorteachingaids.ui.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.Activity
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.data.repository.ActivityRepository
import com.juno.colorteachingaids.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LearningPathViewModel @Inject constructor(
    studentRepository: StudentRepository,
    activityRepository: ActivityRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val studentId: StateFlow<Int> = savedStateHandle.getStateFlow("studentId", 0)

    val student: StateFlow<Student?> = studentId.flatMapLatest { studentId ->
        studentRepository.getStudentById(studentId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activities: StateFlow<List<Activity>> = studentId.flatMapLatest { studentId ->
        activityRepository.getActivitiesForStudent(studentId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
