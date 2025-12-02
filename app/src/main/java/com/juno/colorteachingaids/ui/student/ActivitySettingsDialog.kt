package com.juno.colorteachingaids.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ActivitySettingsDialog(
    onDismiss: () -> Unit,
    viewModel: ActivitySettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Module Settings", style = MaterialTheme.typography.titleLarge)
                SettingSwitch(
                    label = "Sound Effects",
                    checked = uiState.soundEffectsEnabled,
                    onCheckedChange = viewModel::onSoundEffectsChanged
                )
                SettingSwitch(
                    label = "Haptic Feedback",
                    checked = uiState.hapticFeedbackEnabled,
                    onCheckedChange = viewModel::onHapticFeedbackChanged
                )
                SettingSwitch(
                    label = "Read Aloud Descriptions",
                    checked = uiState.readAloudEnabled,
                    onCheckedChange = viewModel::onReadAloudChanged
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mastery Target (Consecutive)", modifier = Modifier.weight(1f))
                    TextField(
                        value = uiState.masteryTarget.toString(),
                        onValueChange = viewModel::onMasteryTargetChanged,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Button(onClick = {
                        viewModel.saveSettings()
                        onDismiss()
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingSwitch(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
