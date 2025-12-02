package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.dao.StudentDao
import com.juno.colorteachingaids.data.local.db.entity.Student
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StudentRepositoryImpl @Inject constructor(
    private val studentDao: StudentDao
) : StudentRepository {

    override suspend fun insertStudent(student: Student) {
        studentDao.insertStudent(student)
    }

    override fun getAllStudents(): Flow<List<Student>> {
        return studentDao.getAllStudents()
    }

    override fun getStudentsForTeacher(teacherId: Int): Flow<List<Student>> {
        return studentDao.getStudentsForTeacher(teacherId)
    }

    override fun getStudentById(studentId: Int): Flow<Student> {
        return studentDao.getStudentById(studentId)
    }

    override suspend fun deleteStudentById(studentId: Int) {
        studentDao.deleteStudentById(studentId)
    }
}
