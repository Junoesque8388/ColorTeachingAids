package com.juno.colorteachingaids.ui.teacher

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.data.local.db.entity.TeacherNote
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TeacherNotesScreen(
    onBack: () -> Unit,
    onNavigateToAddNote: (Int) -> Unit,
    viewModel: TeacherNotesViewModel = hiltViewModel()
) {
    val student by viewModel.student.collectAsState()
    val notes by viewModel.notes.collectAsState()

    TeacherNotesContent(
        student = student,
        notes = notes,
        onBack = onBack,
        onAddNoteClicked = { student?.id?.let { onNavigateToAddNote(it) } },
        onDeleteNote = { viewModel.deleteNote(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherNotesContent(
    student: Student?,
    notes: List<TeacherNote>,
    onBack: () -> Unit,
    onAddNoteClicked: () -> Unit,
    onDeleteNote: (TeacherNote) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("General Teacher's Notes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
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
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            if (notes.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No notes yet. Tap the '+' button to add one.", color = Color.Gray)
                    }
                }
            } else {
                items(notes, key = { it.id }) { note ->
                    NoteCard(note = note, onDelete = { onDeleteNote(note) })
                }
            }
        }
    }
}

@Composable
private fun NoteCard(note: TeacherNote, onDelete: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }
    val formattedDate = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(note.timestamp)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(formattedDate, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(text = { Text("Edit") }, onClick = { /* TODO: Implement Edit */ })
                        DropdownMenuItem(text = { Text("Delete") }, onClick = onDelete)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(note.content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeacherNotesScreenPreview() {
    ColorTeachingAidsTheme {
        TeacherNotesContent(
            student = Student(id = 1, teacherId = 1, name = "Alex Chen", age = 7),
            notes = listOf(
                TeacherNote(1, 1, System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS, "Alex seemed particularly engaged with activities involving tactile objects today. Consider more sensory items in future sessions."),
                TeacherNote(2, 1, System.currentTimeMillis() - (DateUtils.DAY_IN_MILLIS * 3), "A bit distracted this morning. A short break helped to refocus before the color matching game.")
            ),
            onBack = {},
            onAddNoteClicked = {},
            onDeleteNote = {}
        )
    }
}
