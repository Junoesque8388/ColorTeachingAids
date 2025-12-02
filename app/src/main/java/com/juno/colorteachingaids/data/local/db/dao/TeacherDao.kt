package com.juno.colorteachingaids.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juno.colorteachingaids.data.local.db.entity.Teacher
import kotlinx.coroutines.flow.Flow

@Dao
interface TeacherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeacher(teacher: Teacher): Long

    @Query("SELECT * FROM teachers")
    fun getAllTeachers(): Flow<List<Teacher>>

    @Query("SELECT * FROM teachers WHERE id = :teacherId")
    fun getTeacherById(teacherId: Int): Flow<Teacher?>

    @Delete
    suspend fun deleteTeacher(teacher: Teacher)
}
