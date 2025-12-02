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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.data.local.db.entity.TeacherNote
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme

@Composable
fun AddNoteScreen(
    onBack: () -> Unit,
    viewModel: AddNoteViewModel = hiltViewModel()
) {
    var noteContent by remember { mutableStateOf("") }

    AddNoteContent(
        noteContent = noteContent,
        onNoteChange = { noteContent = it },
        onSaveNote = {
            viewModel.saveNote(noteContent)
            onBack()
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteContent(
    noteContent: String,
    onNoteChange: (String) -> Unit,
    onSaveNote: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onSaveNote,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = noteContent.isNotBlank()
            ) {
                Text("Save Note")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = noteContent,
                onValueChange = onNoteChange,
                label = { Text("Enter your note...") },
                modifier = Modifier.fillMaxSize(),
                maxLines = 10
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddNoteScreenPreview() {
    ColorTeachingAidsTheme {
        AddNoteContent(
            noteContent = "The student was particularly engaged today.",
            onNoteChange = {},
            onSaveNote = {},
            onBack = {}
        )
    }
}
