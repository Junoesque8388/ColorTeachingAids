package com.juno.colorteachingaids.ui.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.TeacherNote
import com.juno.colorteachingaids.data.repository.TeacherNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val teacherNoteRepository: TeacherNoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val studentId: Int = savedStateHandle.get<Int>("studentId")!!

    fun saveNote(content: String) {
        viewModelScope.launch {
            val note = TeacherNote(
                studentId = studentId,
                timestamp = System.currentTimeMillis(),
                content = content
            )
            teacherNoteRepository.insertNote(note)
        }
    }
}
