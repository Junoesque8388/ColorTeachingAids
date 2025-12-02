package com.juno.colorteachingaids.ui.teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juno.colorteachingaids.data.local.db.entity.Activity
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.data.local.db.entity.Teacher
import com.juno.colorteachingaids.data.repository.ActivityRepository
import com.juno.colorteachingaids.data.repository.StudentRepository
import com.juno.colorteachingaids.data.repository.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

enum class DateFilter(val displayName: String) {
    TODAY("Today"),
    PAST_WEEK("Past Week"),
    CUSTOM("Custom")
}

@HiltViewModel
class TeacherDashboardViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    teacherRepository: TeacherRepository,
    private val activityRepository: ActivityRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val teacherIdFlow: StateFlow<Int> = savedStateHandle.getStateFlow("teacherId", 0)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _dateFilter = MutableStateFlow(DateFilter.TODAY)
    val dateFilter: StateFlow<DateFilter> = _dateFilter.asStateFlow()

    private val _customStartDate = MutableStateFlow<Long?>(null)
    val customStartDate: StateFlow<Long?> = _customStartDate.asStateFlow()

    private val _customEndDate = MutableStateFlow<Long?>(null)
    val customEndDate: StateFlow<Long?> = _customEndDate.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val teacher: StateFlow<Teacher?> = teacherIdFlow.flatMapLatest { id ->
        if (id > 0) {
            teacherRepository.getTeacherById(id)
        } else {
            flowOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val studentsOfTeacher: StateFlow<List<Student>> = teacherIdFlow.flatMapLatest { teacherId ->
        studentRepository.getStudentsForTeacher(teacherId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val students: StateFlow<List<Student>> = studentsOfTeacher
        .combine(_searchQuery) { students, query ->
            if (query.isBlank()) {
                students
            } else {
                students.filter { it.name.contains(query, ignoreCase = true) }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val recentActivities: StateFlow<List<Activity>> = combine(
        studentsOfTeacher, 
        _dateFilter, 
        _customStartDate, 
        _customEndDate
    ) { students, filter, customStart, customEnd ->
        val studentIds = students.map { it.id }
        if (studentIds.isEmpty()) return@combine emptyList()

        val now = Calendar.getInstance()
        val (startTime, endTime) = when (filter) {
            DateFilter.TODAY -> {
                now.set(Calendar.HOUR_OF_DAY, 0)
                now.set(Calendar.MINUTE, 0)
                now.set(Calendar.SECOND, 0)
                val start = now.timeInMillis
                now.set(Calendar.HOUR_OF_DAY, 23)
                now.set(Calendar.MINUTE, 59)
                now.set(Calendar.SECOND, 59)
                val end = now.timeInMillis
                start to end
            }
            DateFilter.PAST_WEEK -> {
                val end = now.timeInMillis
                now.add(Calendar.DAY_OF_YEAR, -7)
                val start = now.timeInMillis
                start to end
            }
            DateFilter.CUSTOM -> {
                customStart to customEnd
            }
        }

        activityRepository.getActivitiesForStudents(studentIds).first()
            .filter { activity ->
                val afterStart = startTime?.let { activity.timestamp >= it } ?: true
                val beforeEnd = endTime?.let { activity.timestamp <= it } ?: true
                afterStart && beforeEnd
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChanged(filter: DateFilter) {
        _dateFilter.value = filter
    }

    fun setCustomDateRange(startDate: Long?, endDate: Long?) {
        _customStartDate.value = startDate
        _customEndDate.value = endDate
        _dateFilter.value = DateFilter.CUSTOM
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.deleteStudentById(student.id)
        }
    }
}
