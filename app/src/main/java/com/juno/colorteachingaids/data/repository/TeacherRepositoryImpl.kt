package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.dao.TeacherDao
import com.juno.colorteachingaids.data.local.db.entity.Teacher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TeacherRepositoryImpl @Inject constructor(
    private val teacherDao: TeacherDao
) : TeacherRepository {

    override suspend fun insertTeacher(teacher: Teacher): Long {
        return teacherDao.insertTeacher(teacher)
    }

    override fun getAllTeachers(): Flow<List<Teacher>> {
        return teacherDao.getAllTeachers()
    }

    override fun getTeacherById(teacherId: Int): Flow<Teacher?> {
        return teacherDao.getTeacherById(teacherId)
    }

    override suspend fun deleteTeacher(teacher: Teacher) {
        teacherDao.deleteTeacher(teacher)
    }
}
