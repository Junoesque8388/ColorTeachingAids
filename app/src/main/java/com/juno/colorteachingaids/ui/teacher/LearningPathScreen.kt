package com.juno.colorteachingaids.ui.teacher

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.data.local.db.entity.Activity
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun LearningPathScreen(
    onBack: () -> Unit,
    viewModel: LearningPathViewModel = hiltViewModel()
) {
    val student by viewModel.student.collectAsState()
    val activities by viewModel.activities.collectAsState()

    LearningPathContent(
        student = student,
        activities = activities,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPathContent(
    student: Student?,
    activities: List<Activity>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Learning Path") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                student?.let {
                    Text(it.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Complete Activity Log", color = Color.Gray)
                }
            }
            items(activities, key = { it.id }) { activity ->
                ActivityLogCard(activity)
            }
        }
    }
}

@Composable
private fun ActivityLogCard(activity: Activity) {
    val formattedDate = SimpleDateFormat("MMMM d, yyyy, h:mm a", Locale.getDefault()).format(activity.timestamp)
    val durationMinutes = TimeUnit.MILLISECONDS.toMinutes(activity.duration)
    val durationSeconds = TimeUnit.MILLISECONDS.toSeconds(activity.duration) % 60
    val durationString = if (durationMinutes > 0) "${durationMinutes}m ${durationSeconds}s" else "${durationSeconds}s"

    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(activity.moduleName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(formattedDate, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(durationString, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${activity.mistakeCount} mistakes", 
                    style = MaterialTheme.typography.bodySmall, 
                    color = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LearningPathScreenPreview() {
    ColorTeachingAidsTheme {
        LearningPathContent(
            student = Student(id = 1, teacherId = 1, name = "Alex Chen", age = 7),
            activities = listOf(
                Activity(1, 1, "Module 1: Foundations", System.currentTimeMillis() - DateUtils.HOUR_IN_MILLIS, 300000, 3),
                Activity(2, 1, "Module 2: Mix & Create", System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS, 600000, 8)
            ),
            onBack = {}
        )
    }
}
