package com.juno.colorteachingaids.ui.teacher

import android.text.format.DateUtils
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.juno.colorteachingaids.data.local.db.entity.Activity
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme
import com.juno.colorteachingaids.ui.theme.SecondaryAccentLight
import java.util.concurrent.TimeUnit

@Composable
fun TeacherStudentDetailScreen(
    onBack: () -> Unit,
    onLaunchActivity: (Int) -> Unit,
    onEditStudent: (Int) -> Unit,
    onNavigateToLearningPath: (Int) -> Unit,
    onNavigateToTeacherNotes: (Int) -> Unit,
    viewModel: TeacherStudentDetailViewModel = hiltViewModel()
) {
    val student by viewModel.student.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState()
    val moduleProgress by viewModel.moduleProgress.collectAsState()

    TeacherStudentDetailContent(
        student = student,
        recentActivities = recentActivities,
        moduleProgress = moduleProgress,
        onBack = onBack,
        onLaunchActivity = onLaunchActivity,
        onEditStudent = onEditStudent,
        onNavigateToLearningPath = onNavigateToLearningPath,
        onNavigateToTeacherNotes = onNavigateToTeacherNotes
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherStudentDetailContent(
    student: Student?,
    recentActivities: List<Activity>,
    moduleProgress: List<ModuleProgress>,
    onBack: () -> Unit,
    onLaunchActivity: (Int) -> Unit,
    onEditStudent: (Int) -> Unit,
    onNavigateToLearningPath: (Int) -> Unit,
    onNavigateToTeacherNotes: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        student?.let { studentData ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                        ) {
                            AsyncImage(
                                model = studentData.photoUri?.toUri(),
                                contentDescription = "Student Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(studentData.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Text("Student Profile", color = SecondaryAccentLight)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { onEditStudent(studentData.id) }) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Edit Profile")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onLaunchActivity(studentData.id) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Launch Activity")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("At-a-Glance Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                // Summary Cards
                item {
                    val totalTime = recentActivities.sumOf { it.duration }
                    val hours = TimeUnit.MILLISECONDS.toHours(totalTime)
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime) % 60
                    val totalTimeStr = if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"

                    val lastSession = recentActivities.maxByOrNull { it.timestamp }
                    val lastSessionStr = lastSession?.let {
                        DateUtils.getRelativeTimeSpanString(
                            it.timestamp,
                            System.currentTimeMillis(),
                            DateUtils.MINUTE_IN_MILLIS
                        ).toString()
                    } ?: "None"

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(vertical = 16.dp)) {
                        SummaryCard(title = "Total Time", value = totalTimeStr, subtext = "this week", modifier = Modifier.weight(1f))
                        SummaryCard(title = "Activities", value = recentActivities.size.toString(), subtext = "this week", modifier = Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SummaryCard(title = "Current Focus", value = lastSession?.moduleName ?: "None", subtext = "", modifier = Modifier.weight(1f))
                        SummaryCard(title = "Last Session", value = lastSessionStr, subtext = "", modifier = Modifier.weight(1f))
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Progress by Module", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Module Progress
                items(moduleProgress) { progress ->
                    ModuleProgressCard(moduleName = progress.moduleId, progress = progress.progress)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { onNavigateToLearningPath(studentData.id) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Learning Path Record")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onNavigateToTeacherNotes(studentData.id) }, modifier = Modifier.fillMaxWidth()) {
                        Text("General Teacher's Notes")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(title: String, value: String, subtext: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(subtext, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
private fun ModuleProgressCard(moduleName: String, progress: Float) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = if (progress >= 1.0f) Color.Green else MaterialTheme.colorScheme.primary

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(progressColor)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(moduleName, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Canvas(modifier = Modifier.fillMaxWidth().height(4.dp)) {
                    drawLine(
                        color = trackColor,
                        start = Offset(0f, center.y),
                        end = Offset(size.width, center.y),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    if (progress > 0) {
                        drawLine(
                            color = progressColor,
                            start = Offset(0f, center.y),
                            end = Offset(size.width * progress, center.y),
                            strokeWidth = 4.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeacherStudentDetailScreenPreview() {
    ColorTeachingAidsTheme {
        TeacherStudentDetailContent(
            student = Student(id = 1, teacherId = 1, name = "Alex Chen", age = 7),
            recentActivities = emptyList(),
            moduleProgress = listOf(
                ModuleProgress("Module 1: Color Match", 0.66f, 3),
                ModuleProgress("Module 2: Color Mixing", 0.33f, 3),
                ModuleProgress("Module 3: Color Identification", 1.0f, 3)
            ),
            onBack = {},
            onLaunchActivity = {},
            onEditStudent = {},
            onNavigateToLearningPath = {},
            onNavigateToTeacherNotes = {}
        )
    }
}
