package com.juno.colorteachingaids.data.repository

import com.juno.colorteachingaids.data.local.db.dao.ModuleSettingsDao
import com.juno.colorteachingaids.data.local.db.entity.ModuleSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ModuleSettingsRepositoryImpl @Inject constructor(
    private val moduleSettingsDao: ModuleSettingsDao
) : ModuleSettingsRepository {

    override fun getSettingsForModule(studentId: Int, moduleId: String): Flow<ModuleSettings?> {
        return moduleSettingsDao.getSettingsForModule(studentId, moduleId)
    }

    override fun getAllSettingsForStudent(studentId: Int): Flow<List<ModuleSettings>> {
        return moduleSettingsDao.getAllSettingsForStudent(studentId)
    }

    override suspend fun insertOrUpdateSettings(settings: ModuleSettings) {
        moduleSettingsDao.insertOrUpdateSettings(settings)
    }
}
