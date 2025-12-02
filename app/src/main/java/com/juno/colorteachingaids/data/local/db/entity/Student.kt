package com.juno.colorteachingaids.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val teacherId: Int, // Added teacherId to associate with a teacher
    val name: String,
    val age: Int,
    val photoUri: String? = null,
    val schoolName: String? = null,
    val strengths: String? = null,
    val challenges: String? = null,
    val learningProfile: String? = null,
    val notes: String? = null // New field for teacher's notes
)
