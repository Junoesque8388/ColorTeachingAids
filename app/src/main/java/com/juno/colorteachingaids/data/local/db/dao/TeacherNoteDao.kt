package com.juno.colorteachingaids.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juno.colorteachingaids.data.local.db.entity.TeacherNote
import kotlinx.coroutines.flow.Flow

@Dao
interface TeacherNoteDao {

    @Query("SELECT * FROM teacher_notes WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getNotesForStudent(studentId: Int): Flow<List<TeacherNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: TeacherNote)

    @Delete
    suspend fun deleteNote(note: TeacherNote)
}
