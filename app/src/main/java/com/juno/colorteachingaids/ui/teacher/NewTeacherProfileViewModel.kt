package com.juno.colorteachingaids.ui.teacher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.Teacher
import com.juno.colorteachingaids.data.repository.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTeacherProfileViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val teacherId: Int? = savedStateHandle.get<Int>("teacherId")

    private val _teacher = MutableStateFlow<Teacher?>(null)
    val teacher: StateFlow<Teacher?> = _teacher.asStateFlow()

    var isProfileSaved by mutableStateOf(false)
        private set
    var savedTeacherId by mutableStateOf<Int?>(null)
        private set

    init {
        teacherId?.let { id ->
            viewModelScope.launch {
                _teacher.value = teacherRepository.getTeacherById(id).first()
            }
        }
    }

    fun saveTeacher(name: String, schoolName: String) {
        viewModelScope.launch {
            val teacherToSave = _teacher.value?.copy(
                name = name,
                schoolName = schoolName.takeIf { it.isNotBlank() }
            ) ?: Teacher(
                name = name,
                schoolName = schoolName.takeIf { it.isNotBlank() }
            )

            val id = teacherRepository.insertTeacher(teacherToSave)
            savedTeacherId = id.toInt()
            isProfileSaved = true
        }
    }
}
