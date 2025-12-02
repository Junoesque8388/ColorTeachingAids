package com.juno.colorteachingaids.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juno.colorteachingaids.data.local.db.entity.ModuleSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface ModuleSettingsDao {

    @Query("SELECT * FROM module_settings WHERE studentId = :studentId AND moduleId = :moduleId")
    fun getSettingsForModule(studentId: Int, moduleId: String): Flow<ModuleSettings?>

    @Query("SELECT * FROM module_settings WHERE studentId = :studentId")
    fun getAllSettingsForStudent(studentId: Int): Flow<List<ModuleSettings>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: ModuleSettings)
}
