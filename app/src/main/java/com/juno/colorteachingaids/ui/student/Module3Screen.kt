package com.juno.colorteachingaids.ui.student

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.R
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Module3Screen(
    onDone: () -> Unit,
    viewModel: Module3ViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showHelpDialog by remember { mutableStateOf(false) }
    var showDemoDialog by remember { mutableStateOf(false) }

    if (showHelpDialog) {
        Module3Help(
            onDismiss = { showHelpDialog = false },
            onShowDemo = {
                showHelpDialog = false
                showDemoDialog = true
            }
        )
    }

    if (showDemoDialog) {
        DemoVideoDialog(
            videoUri = Uri.parse("asset:///module3demo.webm"),
            onDismiss = { showDemoDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Module 3: Daily Life") },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onDone()
                        onDone()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(Icons.AutoMirrored.Outlined.HelpOutline, contentDescription = "Help")
                    }
                }
            )
        }
    ) {
        if (state.currentQuestion == null && !state.isGameCompleted) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "What Color Is It?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Main content area
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when {
                        state.isGameCompleted -> {
                            Text("Well Done!", style = MaterialTheme.typography.headlineLarge)
                            Text("You have completed all the questions.", style = MaterialTheme.typography.bodyLarge)
                        }
                        state.currentQuestion != null -> {
                            QuestionContent(state = state, viewModel = viewModel)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                // Persistent buttons at the bottom
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { viewModel.playAgain() }) {
                        Text("Play Again")
                    }
                    Button(onClick = {
                        viewModel.onDone()
                        onDone()
                    }) {
                        Text("Done")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun QuestionContent(state: Module3State, viewModel: Module3ViewModel) {
    val question = state.currentQuestion!!
    val imageToShow = if (state.showColorImage) {
        question.correctObject.colorImageRes
    } else {
        question.correctObject.greyImageRes
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
            Image(
                painter = painterResource(id = imageToShow),
                contentDescription = question.correctObject.name,
                modifier = Modifier.size(250.dp),
                contentScale = ContentScale.Crop
            )
        }

        // This Column with a weight will fill the available space and center its content.
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // The feedback message
            AnimatedVisibility(visible = state.feedback?.isCorrect == true) {
                state.studentName?.let {
                    Text(
                        text = "You are correct, ${it}! The ${question.correctObject.name.lowercase()} is ${question.correctObject.colorName.lowercase()}.".trim(),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // The question text, which is hidden when feedback is shown
            AnimatedVisibility(visible = state.feedback == null) {
                Text(
                    text = "What color is this ${question.correctObject.name.lowercase()}?",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            question.colorChoices.forEach { choice ->
                ColorOption(choice = choice, feedback = state.feedback, onClick = { viewModel.onColorSelected(choice) })
            }
        }
    }
}

@Composable
private fun ColorOption(choice: ColorChoice, feedback: Feedback?, onClick: () -> Unit) {
    val isSelected = feedback?.selectedColor == choice.color
    val feedbackColor = when {
        isSelected && feedback.isCorrect -> Color.Green
        isSelected && !feedback.isCorrect -> Color.Red
        else -> choice.color
    }

    val animatedColor by animateColorAsState(
        targetValue = feedbackColor,
        animationSpec = tween(durationMillis = 500),
        label = "Color Animation"
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(animatedColor)
            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable(enabled = feedback == null, onClick = onClick)
    )
}

@Preview(showBackground = true)
@Composable
fun Module3ScreenPreview() {
    ColorTeachingAidsTheme {
        Module3Screen(onDone = {})
    }
}
