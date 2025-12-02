package com.juno.colorteachingaids.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juno.colorteachingaids.data.local.db.entity.Activity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities")
    fun getAllActivities(): Flow<List<Activity>>

    @Query("SELECT * FROM activities WHERE studentId = :studentId ORDER BY timestamp DESC")
    fun getActivitiesForStudent(studentId: Int): Flow<List<Activity>>

    @Query("SELECT * FROM activities WHERE studentId IN (:studentIds) ORDER BY timestamp DESC")
    fun getActivitiesForStudents(studentIds: List<Int>): Flow<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: Activity)
}
