package com.juno.colorteachingaids.di

import com.juno.colorteachingaids.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStudentRepository(studentRepositoryImpl: StudentRepositoryImpl): StudentRepository

    @Binds
    @Singleton
    abstract fun bindTeacherRepository(teacherRepositoryImpl: TeacherRepositoryImpl): TeacherRepository

    @Binds
    @Singleton
    abstract fun bindActivityRepository(activityRepositoryImpl: ActivityRepositoryImpl): ActivityRepository

    @Binds
    @Singleton
    abstract fun bindModuleSettingsRepository(moduleSettingsRepositoryImpl: ModuleSettingsRepositoryImpl): ModuleSettingsRepository

    @Binds
    @Singleton
    abstract fun bindTeacherNoteRepository(teacherNoteRepositoryImpl: TeacherNoteRepositoryImpl): TeacherNoteRepository
}
