package com.juno.colorteachingaids.ui.teacher

// Defines the state for the TeacherStudentDetail screen, kept separate to avoid circular dependencies.
data class ModuleProgress(val moduleId: String, val progress: Float, val target: Int)
