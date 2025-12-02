package com.juno.colorteachingaids.ui.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.data.local.db.entity.Activity
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.data.local.db.entity.Teacher
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TeacherDashboardScreen(
    onNavigateToStudentDashboard: (studentId: Int) -> Unit,
    onNavigateToAddStudent: () -> Unit,
    onNavigateToEditStudent: (studentId: Int) -> Unit,
    viewModel: TeacherDashboardViewModel = hiltViewModel()
) {
    val students by viewModel.students.collectAsState(initial = emptyList())
    val teacher by viewModel.teacher.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState(initial = emptyList())
    val searchQuery by viewModel.searchQuery.collectAsState()
    val dateFilter by viewModel.dateFilter.collectAsState()
    val customStartDate by viewModel.customStartDate.collectAsState()
    val customEndDate by viewModel.customEndDate.collectAsState()

    TeacherDashboardContent(
        teacher = teacher,
        students = students,
        recentActivities = recentActivities,
        searchQuery = searchQuery,
        dateFilter = dateFilter,
        customStartDate = customStartDate,
        customEndDate = customEndDate,
        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
        onFilterChanged = { viewModel.onFilterChanged(it) },
        onCustomDateRangeSelected = { start, end -> viewModel.setCustomDateRange(start, end) },
        onNavigateToStudentDashboard = onNavigateToStudentDashboard,
        onNavigateToAddStudent = onNavigateToAddStudent,
        onNavigateToEditStudent = onNavigateToEditStudent,
        onDeleteStudent = { viewModel.deleteStudent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardContent(
    teacher: Teacher?,
    students: List<Student>,
    recentActivities: List<Activity>,
    searchQuery: String,
    dateFilter: DateFilter,
    customStartDate: Long?,
    customEndDate: Long?,
    onSearchQueryChanged: (String) -> Unit,
    onFilterChanged: (DateFilter) -> Unit,
    onCustomDateRangeSelected: (Long?, Long?) -> Unit,
    onNavigateToStudentDashboard: (studentId: Int) -> Unit,
    onNavigateToAddStudent: () -> Unit,
    onNavigateToEditStudent: (studentId: Int) -> Unit,
    onDeleteStudent: (Student) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Home", "My Students", "Activities", "My Profile")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tabTitles[selectedTab]) } // Dynamic title
            )
        },
        floatingActionButton = {
            if (selectedTab == 1) { // Show FAB only on "My Students" tab
                FloatingActionButton(onClick = onNavigateToAddStudent) {
                    Icon(Icons.Default.Add, contentDescription = "Add Student")
                }
            }
        },
        bottomBar = {
            TeacherBottomNavigation(
                selectedItem = selectedTab,
                onItemSelected = { selectedTab = it },
                tabTitles = tabTitles
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> HomeContent(teacher, students, recentActivities, students.associateBy { it.id })
                1 -> MyStudentsContent(students, searchQuery, onSearchQueryChanged, onNavigateToStudentDashboard, onNavigateToEditStudent, onDeleteStudent)
                2 -> ActivitiesContent(recentActivities, students.associateBy { it.id }, dateFilter, customStartDate, customEndDate, onFilterChanged, onCustomDateRangeSelected)
                3 -> MyProfileContent(teacher)
            }
        }
    }
}

@Composable
private fun HomeContent(teacher: Teacher?, students: List<Student>, recentActivities: List<Activity>, studentMap: Map<Int, Student>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Good day, Teacher ${teacher?.name ?: ""}!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Welcome to your Dashboard!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("This is your central hub for managing students and monitoring their progress.", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Text("What to do next:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text("• Go to 'My Students' to add or view a student profile.", style = MaterialTheme.typography.bodyMedium)
                Text("• Tap on a student to see their individual progress and start an activity.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Recent Activity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        if (recentActivities.isEmpty()) {
            Text("No student activity yet. Add a student to get started!", color = Color.Gray)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(recentActivities.take(5)) { activity -> // Limit to 5 recent items
                    val studentName = studentMap[activity.studentId]?.name ?: "Unknown Student"
                    Text("$studentName completed ${activity.moduleName}.")
                }
            }
        }
    }
}

@Composable
private fun MyStudentsContent(
    students: List<Student>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onNavigateToStudentDashboard: (Int) -> Unit,
    onNavigateToEditStudent: (Int) -> Unit,
    onDeleteStudent: (Student) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SearchBar(query = searchQuery, onQueryChanged = onSearchQueryChanged)
        Spacer(modifier = Modifier.height(16.dp))
        if (students.isEmpty() && searchQuery.isBlank()) {
            EmptyStudentState()
        } else {
            StudentGrid(students = students, onStudentClicked = onNavigateToStudentDashboard, onEditClicked = onNavigateToEditStudent, onDeleteClicked = onDeleteStudent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search students...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear search")
                }
            }
        },
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActivitiesContent(activities: List<Activity>, studentMap: Map<Int, Student>, selectedFilter: DateFilter, customStartDate: Long?, customEndDate: Long?, onFilterChanged: (DateFilter) -> Unit, onCustomDateRangeSelected: (Long?, Long?) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDateRangePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = { 
                    showDatePicker = false
                    onCustomDateRangeSelected(datePickerState.selectedStartDateMillis, datePickerState.selectedEndDateMillis)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DateRangePicker(state = datePickerState)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Overview of all student activities",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DateFilter.values().forEach { filter ->
                    val isSelected = selectedFilter == filter
                    val label = if (filter == DateFilter.CUSTOM && customStartDate != null) {
                        val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
                        "${formatter.format(Date(customStartDate))} - ${formatter.format(Date(customEndDate ?: customStartDate))}"
                    } else {
                        filter.displayName
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = { 
                            if (filter == DateFilter.CUSTOM) {
                                showDatePicker = true
                            } else {
                                onFilterChanged(filter)
                            }
                        },
                        label = { Text(label) }
                    )
                }
            }
        }

        if (activities.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No activities recorded for this period.", color = Color.Gray)
                }
            }
        } else {
            items(activities, key = { it.id }) { activity ->
                val studentName = studentMap[activity.studentId]?.name ?: "Unknown Student"
                ActivityCard(
                    studentName = studentName,
                    activity = activity
                )
            }
        }
    }
}

@Composable
private fun ActivityCard(studentName: String, activity: Activity) {
    val formattedDate = SimpleDateFormat("MMMM d, yyyy, h:mm a", Locale.getDefault()).format(Date(activity.timestamp))
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(studentName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(activity.moduleName, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "${activity.mistakeCount} mistakes",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (activity.mistakeCount > 0) Color.Red else Color.Green
                )
            }
        }
    }
}

@Composable
private fun MyProfileContent(teacher: Teacher?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        teacher?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Teacher Profile", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Name: ${it.name}", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    it.schoolName?.let {
                        Text("School: $it", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStudentState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No students have been added yet.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the '+' button to add your first student.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
private fun StudentGrid(students: List<Student>, onStudentClicked: (Int) -> Unit, onEditClicked: (Int) -> Unit, onDeleteClicked: (Student) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(students) { student ->
            StudentCard(student = student, onClick = { onStudentClicked(student.id) }, onEditClick = { onEditClicked(student.id) }, onDeleteClick = { onDeleteClicked(student) })
        }
    }
}

@Composable
private fun StudentCard(student: Student, onClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(36.dp)) // Space for the icon button
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(student.name, style = MaterialTheme.typography.bodyMedium)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Edit Student")
                }
            }
        }
    }
}


@Composable
private fun TeacherBottomNavigation(selectedItem: Int, onItemSelected: (Int) -> Unit, tabTitles: List<String>) {
    val icons = listOf(Icons.Outlined.Home, Icons.Outlined.Person, Icons.AutoMirrored.Outlined.List, Icons.Outlined.AccountCircle)

    NavigationBar {
        tabTitles.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { onItemSelected(index) }
            )
        }
    }
}

@Preview(showBackground = true, name = "Home Dashboard - Empty")
@Composable
fun TeacherDashboardHomeScreenEmptyPreview() {
    ColorTeachingAidsTheme {
        TeacherDashboardContent(
            teacher = Teacher(id = 1, name = "Andy", schoolName = ""),
            students = emptyList(),
            recentActivities = emptyList(),
            searchQuery = "",
            dateFilter = DateFilter.TODAY,
            customStartDate = null,
            customEndDate = null,
            onSearchQueryChanged = {},
            onFilterChanged = {},
            onCustomDateRangeSelected = { _, _ -> },
            onNavigateToStudentDashboard = {},
            onNavigateToAddStudent = {},
            onNavigateToEditStudent = {},
            onDeleteStudent = {}
        )
    }
}

@Preview(showBackground = true, name = "My Students with Data")
@Composable
fun TeacherDashboardStudentsPreview() {
    var selectedTab by remember { mutableStateOf(1) }
    ColorTeachingAidsTheme {
        Scaffold(
            bottomBar = {
                TeacherBottomNavigation(selectedItem = selectedTab, onItemSelected = { selectedTab = it }, tabTitles = listOf("Home", "My Students", "Activities", "My Profile"))
            }
        ) { paddingValues ->
            Box(Modifier.padding(paddingValues)) {
                MyStudentsContent(
                    students = listOf(Student(id = 1, teacherId = 1, name = "Ben", age = 6)),
                    searchQuery = "",
                    onSearchQueryChanged = {},
                    onNavigateToStudentDashboard = {},
                    onNavigateToEditStudent = {},
                    onDeleteStudent = {}
                )
            }
        }
    }
}
