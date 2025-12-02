package com.juno.colorteachingaids.ui.student

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun DemoVideoDialog(
    videoUri: Uri,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        VideoPlayer(videoUri = videoUri)
    }
}
