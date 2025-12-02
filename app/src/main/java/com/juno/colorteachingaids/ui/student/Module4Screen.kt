package com.juno.colorteachingaids.ui.student

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.ui.theme.*
import java.util.concurrent.Executors
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Module4Screen(
    onBack: () -> Unit,
    viewModel: Module4ViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasPermission = granted
        }
    )

    var showDetailsDialog by remember { mutableStateOf(false) }

    if (showDetailsDialog) {
        Module4DetailsDialog { showDetailsDialog = false }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Module 4: Color Explorer") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDetailsDialog = true }) {
                        Icon(Icons.Outlined.Info, contentDescription = "Details")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (hasPermission) {
                // Only show the CameraView if the dialog is not visible
                if (!showDetailsDialog) {
                    CameraView(viewModel)
                }
            } else {
                PermissionRationale(onRequestPermission = { launcher.launch(Manifest.permission.CAMERA) })
            }
        }
    }
}

@Composable
private fun PermissionRationale(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Camera Required", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("This module needs camera access to help you explore colors in the world.", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Grant Permission")
        }
    }
}


@Composable
private fun CameraView(viewModel: Module4ViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val uiState by viewModel.uiState.collectAsState()

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = Executors.newSingleThreadExecutor()
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = androidx.camera.core.Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(executor, ColorAnalyzer { color ->
                            val newColorName = ColorUtils.findClosestColor(color)
                            viewModel.onColorDetected(newColorName)
                        })
                    }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = Modifier.fillMaxSize(),
    )

    // UI Overlay
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.5f),
                radius = 20.dp.toPx(), // Changed radius from 40.dp to 20.dp
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()),
                center = center
            )
        }
        Text(
            text = uiState.detectedColorName,
            modifier = Modifier.align(Alignment.BottomCenter).padding(32.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}


private class ColorAnalyzer(private val onColorAnalyzed: (Color) -> Unit) : ImageAnalysis.Analyzer {
    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val bitmap = image.toBitmap()

        // Get color from the center pixel
        val centerX = bitmap.width / 2
        val centerY = bitmap.height / 2
        val centerPixel = bitmap.getPixel(centerX, centerY)
        val centerColor = Color(centerPixel)

        onColorAnalyzed(centerColor)

        image.close()
    }
}

object ColorUtils {
    private val colorMap = mapOf(
        "Red" to ContentRed,
        "Green" to ContentGreen,
        "Blue" to ContentBlue,
        "Yellow" to ContentYellow,
        "Orange" to ContentOrange,
        "Purple" to ContentPurple,
        "White" to Color.White,
        "Black" to Color.Black,
        "Gray" to Color.Gray,
        "Brown" to Color(0xFFA52A2A),
        "Pink" to Color(0xFFFFC0CB),
        "Light Blue" to Color(0xFFADD8E6),
        "Dark Blue" to Color(0xFF00008B),
        "Gold" to Color(0xFFFFD700),
        "Silver" to Color(0xFFC0C0C0),
        "Dark Green" to Color(0xFF006400),
        "Light Green" to Color(0xFF90EE90),
        "Light Purple" to Color(0xFFE6E6FA)
    )

    fun findClosestColor(color: Color): String {
        return colorMap.minByOrNull { (name, refColor) -> colorDistance(color, refColor) }?.key ?: "Unknown"
    }

    // Using a simple RGB distance formula for performance
    private fun colorDistance(c1: Color, c2: Color): Int {
        val redDiff = abs(c1.red - c2.red) * 255
        val greenDiff = abs(c1.green - c2.green) * 255
        val blueDiff = abs(c1.blue - c2.blue) * 255
        return (redDiff + greenDiff + blueDiff).toInt()
    }
}
