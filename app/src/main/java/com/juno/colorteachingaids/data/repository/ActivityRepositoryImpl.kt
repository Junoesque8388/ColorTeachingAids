package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.dao.ActivityDao
import com.juno.colorteachingaids.data.local.db.entity.Activity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val activityDao: ActivityDao
) : ActivityRepository {

    override fun getAllActivities(): Flow<List<Activity>> {
        return activityDao.getAllActivities()
    }

    override fun getActivitiesForStudent(studentId: Int): Flow<List<Activity>> {
        return activityDao.getActivitiesForStudent(studentId)
    }

    override fun getActivitiesForStudents(studentIds: List<Int>): Flow<List<Activity>> {
        return activityDao.getActivitiesForStudents(studentIds)
    }

    override suspend fun insertActivity(activity: Activity) {
        activityDao.insertActivity(activity)
    }
}
