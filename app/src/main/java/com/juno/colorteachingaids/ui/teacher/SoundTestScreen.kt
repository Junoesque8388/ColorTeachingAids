package com.juno.colorteachingaids.ui.teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.R

@Composable
fun SoundTestScreen(viewModel: SoundTestViewModel = hiltViewModel()) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Sound Test Screen")
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { viewModel.playSound(R.raw.item_pop) }) {
                Text("Play Item Pop")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.playSound(R.raw.correct_chime) }) {
                Text("Play Correct Chime")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.playSound(R.raw.color_splash) }) {
                Text("Play Color Splash")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.playSound(R.raw.clear_whoosh) }) {
                Text("Play Clear Whoosh")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.playSound(R.raw.correct_ding) }) {
                Text("Play Correct Ding")
            }
        }
    }
}
