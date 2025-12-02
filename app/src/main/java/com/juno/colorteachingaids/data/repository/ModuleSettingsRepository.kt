package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.entity.ModuleSettings
import kotlinx.coroutines.flow.Flow

interface ModuleSettingsRepository {
    fun getSettingsForModule(studentId: Int, moduleId: String): Flow<ModuleSettings?>
    fun getAllSettingsForStudent(studentId: Int): Flow<List<ModuleSettings>>
    suspend fun insertOrUpdateSettings(settings: ModuleSettings)
}
