package com.juno.colorteachingaids.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val studentId: Int,
    val moduleName: String,
    val timestamp: Long, // The time the activity was completed
    val duration: Long, // The duration of the activity in milliseconds
    val mistakeCount: Int // New field for tracking mistakes
)
