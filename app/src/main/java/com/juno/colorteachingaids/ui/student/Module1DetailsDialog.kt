package com.juno.colorteachingaids.ui.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme

@Composable
fun Module1DetailsDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                IconButton(onClick = onDismiss, modifier = Modifier.padding(bottom = 8.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
                Text("Module 1: Foundations (Color Recognition)", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Module Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("This activity helps students recognize 6 core colors through matching real-world objects.")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Best For", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("• Students who are just beginning to learn colors")
                Text("• Non-verbal learners")
                Text("• Learners with visual processing differences")
                Spacer(modifier = Modifier.height(16.dp))
                Text("IEP Alignment", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("\"Student will identify 6 basic colors in structured tasks with 80% accuracy.\"")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Customization Tips", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("• Turn off sound for noise-sensitive students.")
                Text("• Use Slow animation for students with attention delays.")
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Got it")
                }
            }
        }
    }
}

@Preview
@Composable
fun Module1DetailsDialogPreview() {
    ColorTeachingAidsTheme {
        Module1DetailsDialog(onDismiss = {})
    }
}
