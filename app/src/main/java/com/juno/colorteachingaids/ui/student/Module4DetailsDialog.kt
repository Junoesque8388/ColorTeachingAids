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
fun Module4DetailsDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                IconButton(onClick = onDismiss, modifier = Modifier.padding(bottom = 8.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
                Text("Module 4: Color Explorer", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Module Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("This activity uses the camera to help students find and identify colors in their own environment, making learning tangible and fun.")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Best For", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("• Generalizing color knowledge to the real world")
                Text("• Kinesthetic learners who learn by doing and exploring")
                Text("• Promoting engagement through technology")
                Spacer(modifier = Modifier.height(16.dp))
                Text("IEP Alignment", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("\"Student will identify and name 5 different colors when presented with real-world objects in their environment.\"")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Customization Tips", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("• Encourage the student to find objects of a specific color ('Can you find something blue?')")
                Text("• Use in different environments (classroom, outdoors, home) to reinforce generalization.")
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
fun Module4DetailsDialogPreview() {
    ColorTeachingAidsTheme {
        Module4DetailsDialog(onDismiss = {})
    }
}
