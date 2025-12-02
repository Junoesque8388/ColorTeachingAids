package com.juno.colorteachingaids.ui.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddStudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // studentId will be > 0 in edit mode, and 0 in add mode.
    private val studentId: Int = savedStateHandle.get<Int>("studentId") ?: 0

    // teacherIdForNewStudent will only have a value in add mode.
    private val teacherIdForNewStudent: Int? = savedStateHandle.get<Int>("teacherId")

    val student: StateFlow<Student?> = if (studentId > 0) {
        studentRepository.getStudentById(studentId)
    } else {
        flowOf(null)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveStudent(formData: StudentFormData) {
        viewModelScope.launch {
            val studentBeingEdited = student.value

            // When editing, use the student's existing teacherId.
            // When adding, use the teacherId passed in the navigation arguments.
            val finalTeacherId = studentBeingEdited?.teacherId ?: teacherIdForNewStudent

            // A student must have a teacher, so we only save if we have a valid teacherId.
            if (finalTeacherId != null) {
                val studentToSave = Student(
                    id = studentId, // If 0, Room will generate a new ID.
                    teacherId = finalTeacherId,
                    name = "${formData.firstName} ${formData.lastName}",
                    age = 0, // Age is not currently in the form, default to 0
                    photoUri = formData.photoUri,
                    schoolName = formData.schoolName,
                    strengths = formData.strengths,
                    challenges = formData.challenges,
                    learningProfile = formData.learningProfile.joinToString(","),
                    notes = studentBeingEdited?.notes // Preserve notes when editing
                )
                studentRepository.insertStudent(studentToSave)
            }
        }
    }
}
