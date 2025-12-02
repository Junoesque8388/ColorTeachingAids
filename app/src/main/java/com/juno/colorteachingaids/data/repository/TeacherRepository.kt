package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.entity.Teacher
import kotlinx.coroutines.flow.Flow

interface TeacherRepository {

    suspend fun insertTeacher(teacher: Teacher): Long

    fun getAllTeachers(): Flow<List<Teacher>>

    fun getTeacherById(teacherId: Int): Flow<Teacher?>

    suspend fun deleteTeacher(teacher: Teacher)
}
