package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.entity.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {

    suspend fun insertStudent(student: Student)

    fun getAllStudents(): Flow<List<Student>>

    fun getStudentsForTeacher(teacherId: Int): Flow<List<Student>>

    fun getStudentById(studentId: Int): Flow<Student>

    suspend fun deleteStudentById(studentId: Int)
}
