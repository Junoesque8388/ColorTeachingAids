package com.juno.colorteachingaids.data.local.db.entity

import androidx.room.Entity

@Entity(
    tableName = "module_settings",
    primaryKeys = ["studentId", "moduleId"]
)
data class ModuleSettings(
    val studentId: Int,
    val moduleId: String,
    val soundEffectsEnabled: Boolean = false,
    val hapticFeedbackEnabled: Boolean = false,
    val readAloudEnabled: Boolean = false,
    val masteryTarget: Int = 3
)
