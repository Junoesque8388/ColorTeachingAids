package com.juno.colorteachingaids.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teacher_notes")
data class TeacherNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val studentId: Int,
    val timestamp: Long,
    val content: String
)
