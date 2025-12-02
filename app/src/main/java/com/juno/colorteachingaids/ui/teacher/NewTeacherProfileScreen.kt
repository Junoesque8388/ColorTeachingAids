package com.juno.colorteachingaids.ui.teacher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme

@Composable
fun NewTeacherProfileScreen(
    onProfileCreated: (Int) -> Unit,
    onBack: () -> Unit = {},
    viewModel: NewTeacherProfileViewModel = hiltViewModel()
) {
    val teacher by viewModel.teacher.collectAsState()
    var name by remember { mutableStateOf("") }
    var schoolName by remember { mutableStateOf("") }

    LaunchedEffect(teacher) {
        teacher?.let {
            name = it.name
            schoolName = it.schoolName ?: ""
        }
    }

    NewTeacherProfileContent(
        name = name,
        schoolName = schoolName,
        isEditMode = teacher != null,
        onNameChanged = { name = it },
        onSchoolNameChanged = { schoolName = it },
        onSave = {
            viewModel.saveTeacher(name, schoolName)
        },
        onBack = onBack
    )

    LaunchedEffect(viewModel.isProfileSaved) {
        if (viewModel.isProfileSaved) {
            viewModel.savedTeacherId?.let { onProfileCreated(it) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTeacherProfileContent(
    name: String,
    schoolName: String,
    isEditMode: Boolean,
    onNameChanged: (String) -> Unit,
    onSchoolNameChanged: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Edit Profile" else "Create New Profile") },
                navigationIcon = {
                    // Only show back arrow in edit mode or if onBack is provided
                    if (isEditMode || onBack != {}) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChanged,
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = schoolName,
                onValueChange = onSchoolNameChanged,
                label = { Text("School Name (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text("Save Profile")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewTeacherProfileScreenPreview() {
    ColorTeachingAidsTheme {
        NewTeacherProfileContent(
            name = "John Doe",
            schoolName = "Springfield Elementary",
            isEditMode = false,
            onNameChanged = {},
            onSchoolNameChanged = {},
            onSave = {},
            onBack = {}
        )
    }
}
