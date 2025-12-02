package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.entity.TeacherNote
import kotlinx.coroutines.flow.Flow

interface TeacherNoteRepository {
    fun getNotesForStudent(studentId: Int): Flow<List<TeacherNote>>
    suspend fun insertNote(note: TeacherNote)
    suspend fun deleteNote(note: TeacherNote)
}
