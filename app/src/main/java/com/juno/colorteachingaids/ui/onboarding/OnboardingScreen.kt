package com.juno.colorteachingaids.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme
import com.juno.colorteachingaids.ui.theme.PrimaryAccentLight
import com.juno.colorteachingaids.ui.theme.SecondaryAccentLight

@Composable
fun OnboardingScreen(
    onNavigateToAddStudent: () -> Unit,
    onNavigateToTeacherDashboard: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome, Teacher!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "This app is designed to help special learners identify and understand colors through simple, focused activities.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Your first step is to create a profile for your student. This will allow you to track their progress and customize their experience.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = onNavigateToAddStudent,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccentLight)
        ) {
            Text(text = "Add Your First Student", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onNavigateToTeacherDashboard) {
            Text(
                text = "Skip for now, I\'ll do this later.",
                color = SecondaryAccentLight,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    ColorTeachingAidsTheme {
        OnboardingScreen(onNavigateToAddStudent = {}, onNavigateToTeacherDashboard = {})
    }
}
