package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.dao.TeacherNoteDao
import com.juno.colorteachingaids.data.local.db.entity.TeacherNote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TeacherNoteRepositoryImpl @Inject constructor(
    private val teacherNoteDao: TeacherNoteDao
) : TeacherNoteRepository {

    override fun getNotesForStudent(studentId: Int): Flow<List<TeacherNote>> {
        return teacherNoteDao.getNotesForStudent(studentId)
    }

    override suspend fun insertNote(note: TeacherNote) {
        teacherNoteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: TeacherNote) {
        teacherNoteDao.deleteNote(note)
    }
}
