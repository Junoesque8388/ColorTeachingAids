package com.juno.colorteachingaids.ui.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.data.local.db.entity.Teacher
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme

@Composable
fun ExistingTeacherScreen(
    onBack: () -> Unit,
    onTeacherSelected: (Int) -> Unit,
    onNavigateToEditTeacher: (Int) -> Unit,
    viewModel: ExistingTeacherViewModel = hiltViewModel()
) {
    val teachers by viewModel.teachers.collectAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf<Teacher?>(null) }

    showDeleteDialog?.let { teacher ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Profile") },
            text = { Text("Are you sure you want to delete the profile for ${teacher.name}?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteTeacher(teacher)
                    showDeleteDialog = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    ExistingTeacherContent(
        teachers = teachers,
        onBack = onBack,
        onTeacherSelected = onTeacherSelected,
        onDeleteClicked = { showDeleteDialog = it },
        onEditClicked = onNavigateToEditTeacher
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTeacherContent(
    teachers: List<Teacher>,
    onBack: () -> Unit,
    onTeacherSelected: (Int) -> Unit,
    onDeleteClicked: (Teacher) -> Unit,
    onEditClicked: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Your Profile") },
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
                .padding(16.dp)
        ) {
            items(teachers) { teacher ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onTeacherSelected(teacher.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = teacher.name,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onEditClicked(teacher.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                        }
                        IconButton(onClick = { onDeleteClicked(teacher) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Profile")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExistingTeacherScreenPreview() {
    ColorTeachingAidsTheme {
        ExistingTeacherContent(
            teachers = listOf(
                Teacher(id = 1, name = "John Doe", schoolName = "Springfield Elementary"),
                Teacher(id = 2, name = "Jane Smith", schoolName = "Shelbyville Elementary")
            ),
            onBack = {},
            onTeacherSelected = {},
            onDeleteClicked = {},
            onEditClicked = {}
        )
    }
}
