package com.juno.colorteachingaids.ui.student

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(videoUri: Uri) {
    val context = LocalContext.current

    // 1. Create and remember the ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    // 2. Use DisposableEffect for cleanup
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // 3. Use AndroidView with factory and update blocks correctly
    AndroidView(
        factory = {
            // The factory block is for CREATING the view
            PlayerView(it).apply {
                player = exoPlayer
            }
        },
        update = { playerView ->
            // The update block is for SETTING the data after the view is created
            val mediaItem = MediaItem.fromUri(videoUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
        }
    )
}
