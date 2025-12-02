package com.juno.colorteachingaids.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme

@Composable
fun Module2Help(
    onDismiss: () -> Unit,
    onShowDemo: () -> Unit,
    viewModel: Module2HelpViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val helpText = """
        Use the buttons at the top to select a mode:\n\n
        • Basic: Drag two primary colors (Red, Yellow, or Blue) into the bowl to create a new secondary color.\n\n
        • Tone: Drag White or Black into the bowl with another color to make it lighter or darker.\n\n
        • Hue: After mixing two colors, drag more of one of the primary colors to change the hue of the new color.\n\n
        • Multiple: Mix all the colors together freely, just like real paint!\n\n
        Use the ‘Clear Bowl’ button to start over.
    """

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("How to Play", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(modifier = Modifier.verticalScroll(scrollState).weight(1f, fill = false)) {
                    Text(helpText)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { viewModel.onReadAloud(helpText) }, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Read Aloud")
                        Spacer(modifier = Modifier.padding(start = 8.dp))
                        Text("Read Aloud")
                    }
                    Button(onClick = onShowDemo, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Demo")
                        Spacer(modifier = Modifier.padding(start = 8.dp))
                        Text("Demo")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Close")
                }
            }
        }
    }
}

@Preview
@Composable
fun Module2HelpPreview() {
    ColorTeachingAidsTheme {
        // This preview is simplified and will not have working TTS
        Module2Help(onDismiss = {}, onShowDemo = {})
    }
}
