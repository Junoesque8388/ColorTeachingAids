package com.juno.colorteachingaids.di

import android.content.Context
import androidx.room.Room
import com.juno.colorteachingaids.data.local.db.AppDatabase
import com.juno.colorteachingaids.data.local.db.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideStudentDao(appDatabase: AppDatabase): StudentDao {
        return appDatabase.studentDao()
    }

    @Provides
    @Singleton
    fun provideTeacherDao(appDatabase: AppDatabase): TeacherDao {
        return appDatabase.teacherDao()
    }

    @Provides
    @Singleton
    fun provideActivityDao(appDatabase: AppDatabase): ActivityDao {
        return appDatabase.activityDao()
    }

    @Provides
    @Singleton
    fun provideModuleSettingsDao(appDatabase: AppDatabase): ModuleSettingsDao {
        return appDatabase.moduleSettingsDao()
    }

    @Provides
    @Singleton
    fun provideTeacherNoteDao(appDatabase: AppDatabase): TeacherNoteDao {
        return appDatabase.teacherNoteDao()
    }
}
