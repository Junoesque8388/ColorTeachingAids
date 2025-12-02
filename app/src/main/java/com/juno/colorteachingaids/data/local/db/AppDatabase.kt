package com.juno.colorteachingaids.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juno.colorteachingaids.data.local.db.dao.*
import com.juno.colorteachingaids.data.local.db.entity.*

@Database(
    entities = [Student::class, Teacher::class, Activity::class, ModuleSettings::class, TeacherNote::class],
    version = 4, // Incremented version for schema change
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao
    abstract fun teacherDao(): TeacherDao
    abstract fun activityDao(): ActivityDao
    abstract fun moduleSettingsDao(): ModuleSettingsDao
    abstract fun teacherNoteDao(): TeacherNoteDao

    companion object {
        const val DATABASE_NAME = "color_teaching_aids_db"
    }
}
