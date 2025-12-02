package com.juno.colorteachingaids

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.juno.colorteachingaids.ui.NavGraph
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            ColorTeachingAidsTheme {
                NavGraph()
            }
        }
    }
}
