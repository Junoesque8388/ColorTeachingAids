package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.entity.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getAllActivities(): Flow<List<Activity>>
    fun getActivitiesForStudent(studentId: Int): Flow<List<Activity>>
    fun getActivitiesForStudents(studentIds: List<Int>): Flow<List<Activity>>
    suspend fun insertActivity(activity: Activity)
}
