package com.juno.colorteachingaids.ui.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.data.local.db.entity.TeacherNote
import com.juno.colorteachingaids.data.repository.StudentRepository
import com.juno.colorteachingaids.data.repository.TeacherNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherNotesViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val teacherNoteRepository: TeacherNoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val studentId: StateFlow<Int> = savedStateHandle.getStateFlow("studentId", 0)

    val student: StateFlow<Student?> = studentId.flatMapLatest { studentId ->
        studentRepository.getStudentById(studentId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val notes: StateFlow<List<TeacherNote>> = studentId.flatMapLatest { studentId ->
        teacherNoteRepository.getNotesForStudent(studentId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteNote(note: TeacherNote) {
        viewModelScope.launch {
            teacherNoteRepository.deleteNote(note)
        }
    }
}
