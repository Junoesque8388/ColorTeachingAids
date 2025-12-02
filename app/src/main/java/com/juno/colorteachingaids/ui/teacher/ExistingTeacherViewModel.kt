package com.juno.colorteachingaids.ui.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.Teacher
import com.juno.colorteachingaids.data.repository.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExistingTeacherViewModel @Inject constructor(
    private val teacherRepository: TeacherRepository
) : ViewModel() {

    val teachers = teacherRepository.getAllTeachers()

    fun deleteTeacher(teacher: Teacher) {
        viewModelScope.launch {
            teacherRepository.deleteTeacher(teacher)
        }
    }
}
