package com.juno.colorteachingaids.ui.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Settings") },
        text = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sound Effects")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = settingsUiState.soundEffectsEnabled,
                        onCheckedChange = { settingsViewModel.onSoundEffectsChanged(it) }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Haptic Feedback")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = settingsUiState.hapticFeedbackEnabled,
                        onCheckedChange = { settingsViewModel.onHapticFeedbackChanged(it) }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Animation Speed")
                Slider(
                    value = settingsUiState.animationSpeed,
                    onValueChange = { settingsViewModel.onAnimationSpeedChanged(it) },
                    valueRange = 0.5f..1.5f,
                    steps = 1
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}
