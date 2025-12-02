package com.juno.colorteachingaids.ui.student

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityLauncherViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val studentId: StateFlow<Int> = savedStateHandle.getStateFlow("studentId", 0)

    val student: StateFlow<Student?> = studentId.flatMapLatest {
        studentRepository.getStudentById(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateSettings(updatedStudent: Student) {
        viewModelScope.launch {
            studentRepository.insertStudent(updatedStudent)
        }
    }
}
