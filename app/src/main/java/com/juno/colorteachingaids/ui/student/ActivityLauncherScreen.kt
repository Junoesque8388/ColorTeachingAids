package com.juno.colorteachingaids.ui.student

import android.app.Activity
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme
import com.juno.colorteachingaids.ui.theme.ContentBlue
import com.juno.colorteachingaids.ui.theme.ContentGreen
import com.juno.colorteachingaids.ui.theme.ContentOrange
import com.juno.colorteachingaids.ui.theme.ContentPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLauncherScreen(
    onNavigateToModule1: (Int) -> Unit,
    onNavigateToModule2: (Int) -> Unit,
    onNavigateToModule3: (Int) -> Unit,
    onNavigateToModule4: (Int) -> Unit,
    onNavigateToSettings: (studentId: Int, moduleId: String) -> Unit,
    onBack: () -> Unit,
    onExit: () -> Unit,
    viewModel: ActivityLauncherViewModel = hiltViewModel()
) {
    val student by viewModel.student.collectAsState()
    var showModule1Details by remember { mutableStateOf(false) }
    var showModule2Details by remember { mutableStateOf(false) }
    var showModule3Details by remember { mutableStateOf(false) }
    var showModule4Details by remember { mutableStateOf(false) }

    if (showModule1Details) {
        Module1DetailsDialog(onDismiss = { showModule1Details = false })
    }
    if (showModule2Details) {
        Module2DetailsDialog(onDismiss = { showModule2Details = false })
    }
    if (showModule3Details) {
        Module3DetailsDialog(onDismiss = { showModule3Details = false })
    }
    if (showModule4Details) {
        Module4DetailsDialog(onDismiss = { showModule4Details = false })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Learning Activities") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Dashboard")
                    }
                },
                actions = {
                    IconButton(onClick = onExit) {
                        Icon(Icons.Default.Close, contentDescription = "Exit Student Mode")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    ActivityCard(
                        module = "Module 1",
                        title = "Foundations",
                        description = "Match real objects to their color.",
                        color = ContentGreen,
                        onLaunch = { student?.id?.let { onNavigateToModule1(it) } },
                        onDetails = { showModule1Details = true },
                        onSettings = { student?.id?.let { onNavigateToSettings(it, "Module 1") } }
                    )
                }
                item {
                    ActivityCard(
                        module = "Module 2",
                        title = "Mix & Create",
                        description = "Discover how colors mix to make new ones.",
                        color = ContentBlue,
                        onLaunch = { student?.id?.let { onNavigateToModule2(it) } },
                        onDetails = { showModule2Details = true },
                        onSettings = { student?.id?.let { onNavigateToSettings(it, "Module 2") } }
                    )
                }
                item {
                    ActivityCard(
                        module = "Module 3",
                        title = "Color in Daily Life",
                        description = "Find colors in everyday things like food and clothes.",
                        color = ContentOrange,
                        onLaunch = { student?.id?.let { onNavigateToModule3(it) } },
                        onDetails = { showModule3Details = true },
                        onSettings = { student?.id?.let { onNavigateToSettings(it, "Module 3") } }
                    )
                }
                item {
                    ActivityCard(
                        module = "Module 4",
                        title = "Color Explorer",
                        description = "Use the camera to find and identify colors in the world around you.",
                        color = ContentPurple,
                        onLaunch = { student?.id?.let { onNavigateToModule4(it) } },
                        onDetails = { showModule4Details = true },
                        onSettings = { student?.id?.let { onNavigateToSettings(it, "Module 4") } }
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityCard(
    module: String,
    title: String,
    description: String,
    color: Color,
    onLaunch: () -> Unit,
    onDetails: () -> Unit,
    onSettings: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(module, style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
                IconButton(onClick = onSettings) {
                    Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = onLaunch, modifier = Modifier.weight(1f)) {
                    Text("Launch Activity")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = onDetails, modifier = Modifier.weight(1f)) {
                    Text("Details")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityLauncherScreenPreview() {
    ColorTeachingAidsTheme {
        ActivityLauncherScreen({_ -> }, {_ -> }, {_ -> }, {_ -> }, { _, _ -> }, onBack = {}, onExit = {})
    }
}
