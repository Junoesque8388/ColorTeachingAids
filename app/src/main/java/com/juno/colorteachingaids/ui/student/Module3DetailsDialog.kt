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
fun Module3DetailsDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                IconButton(onClick = onDismiss, modifier = Modifier.padding(bottom = 8.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
                Text("Module 3: Color in Daily Life", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Module Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("This activity helps students connect abstract color concepts to real-world objects they see every day—like food, clothing, and nature—to build functional generalizable skills.")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Best For", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("• Students ready to apply color knowledge beyond flashcards")
                Text("• Non-verbal learners who learn best through visual context")
                Text("• Learners with intellectual disability (ID) or autism (ASD)")
                Spacer(modifier = Modifier.height(16.dp))
                Text("IEP Alignment", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("\"Student will identify colors of common objects in the classroom with 75% accuracy.\"")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Customization Tips", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("• Use real photos of the student’s own items for personalization")
                Text("• Pair with a classroom scavenger hunt (\"Find something blue\")")
                Text("• Use Calm Mode for overstimulated students")
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
fun Module3DetailsDialogPreview() {
    ColorTeachingAidsTheme {
        Module3DetailsDialog(onDismiss = {})
    }
}
