package com.juno.colorteachingaids.ui.teacher

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.juno.colorteachingaids.data.local.db.entity.Student
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme

@Composable
fun AddStudentScreen(
    onBack: () -> Unit,
    onStudentAdded: () -> Unit,
    viewModel: AddStudentViewModel = hiltViewModel()
) {
    val student by viewModel.student.collectAsState()

    AddStudentContent(
        student = student,
        onBack = onBack,
        onStudentSaved = {
            viewModel.saveStudent(it)
            onStudentAdded()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentContent(
    student: Student?,
    onBack: () -> Unit,
    onStudentSaved: (StudentFormData) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var schoolName by remember { mutableStateOf("") }
    var strengths by remember { mutableStateOf("") }
    var challenges by remember { mutableStateOf("") }
    val learningProfile = remember { mutableStateListOf<String>() }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // When the student data is loaded, update the form fields
    LaunchedEffect(student) {
        if (student != null) {
            val names = student.name.split(" ", limit = 2)
            firstName = names.getOrElse(0) { "" }
            lastName = names.getOrElse(1) { "" }
            schoolName = student.schoolName ?: ""
            strengths = student.strengths ?: ""
            challenges = student.challenges ?: ""
            student.learningProfile?.split(",")?.let { learningProfile.addAll(it) }
            imageUri = student.photoUri?.toUri()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri = uri }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (student == null) "Add New Student" else "Edit Student") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Reset to Preset")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        onStudentSaved(
                            StudentFormData(
                                firstName,
                                lastName,
                                schoolName,
                                strengths,
                                challenges,
                                learningProfile,
                                imageUri?.toString()
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = firstName.isNotBlank() && lastName.isNotBlank()
                ) {
                    Text("Done")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SectionTitle("The Basics")
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri == null) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Photo",
                            modifier = Modifier.size(40.dp),
                            tint = Color.Gray
                        )
                    } else {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Student Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { launcher.launch("image/*") }) {
                    Text("Add Photo", style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = schoolName, onValueChange = { schoolName = it }, label = { Text("School/District Name (Optional)") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle("Learning Profile")
            Text("Select applicable conditions", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            LearningProfileChips(learningProfile)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = strengths, onValueChange = { strengths = it }, label = { Text("Strengths") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = challenges, onValueChange = { challenges = it }, label = { Text("Challenges / Support Needs") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

data class StudentFormData(
    val firstName: String,
    val lastName: String,
    val schoolName: String?,
    val strengths: String?,
    val challenges: String?,
    val learningProfile: List<String>,
    val photoUri: String?
)

@Composable
private fun SectionTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LearningProfileChips(selected: MutableList<String>) {
    val conditions = listOf("ASD", "ADHD", "DHH", "VI", "SPD")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(conditions.size) {
            val condition = conditions[it]
            val isSelected = selected.contains(condition)
            FilterChip(
                selected = isSelected,
                onClick = {
                    if (isSelected) selected.remove(condition) else selected.add(condition)
                },
                label = { Text(condition) }
            )
        }
    }
}

@Preview(showBackground = true, name = "Add Student Mode")
@Composable
fun AddStudentScreenPreview() {
    ColorTeachingAidsTheme {
        AddStudentContent(student = null, onBack = {}, onStudentSaved = {})
    }
}

@Preview(showBackground = true, name = "Edit Student Mode")
@Composable
fun EditStudentScreenPreview() {
    ColorTeachingAidsTheme {
        AddStudentContent(
            student = Student(
                id = 1,
                teacherId = 1,
                name = "Ali Ramli", 
                age = 0, 
                photoUri = null,
                schoolName = "SMK Seri Indah",
                strengths = "Excellent visual memory...",
                challenges = "Sensitive to loud sounds...",
                learningProfile = "ASD,ADHD",
                notes = ""
            ),
            onBack = {},
            onStudentSaved = {}
        )
    }
}
