package com.juno.colorteachingaids.ui.student

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme

// --- DRAG & DROP STATE ---

class DragAndDropState {
    var isDragging by mutableStateOf(false)
    var dragOffset by mutableStateOf(Offset.Zero)
    var itemStartPosition by mutableStateOf(Offset.Zero)
    var draggedPaint by mutableStateOf<PaintColor?>(null)
    var dropTargetBounds by mutableStateOf(androidx.compose.ui.geometry.Rect.Zero)
}

@Composable
fun rememberDragAndDropState(): DragAndDropState = remember { DragAndDropState() }

// --- MAIN SCREEN ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Module2Screen(
    onDone: () -> Unit,
    viewModel: Module2ViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showHelpDialog by remember { mutableStateOf(false) }
    var showDemoDialog by remember { mutableStateOf(false) }
    val dragAndDropState = rememberDragAndDropState()

    if (showHelpDialog) {
        Module2Help(
            onDismiss = { showHelpDialog = false },
            onShowDemo = { showHelpDialog = false; showDemoDialog = true }
        )
    }

    if (showDemoDialog) {
        DemoVideoDialog(
            videoUri = Uri.parse("asset:///module2demo.webm"),
            onDismiss = { showDemoDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Module 2: Mix & Create") },
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.onDone()
                            onDone()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showHelpDialog = true }) {
                            Icon(Icons.AutoMirrored.Outlined.HelpOutline, contentDescription = "Help")
                        }
                    }
                )
            }
        ) { padding ->
            val currentMode = uiState.mode
            if (currentMode == null) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Let's Mix Colors!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Drag colors into the mixing bowl.", color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    ModeSelector(
                        selectedMode = currentMode,
                        modes = viewModel.modes,
                        onModeSelected = { viewModel.changeMode(it) }
                    )

                    Spacer(modifier = Modifier.weight(0.2f))

                    MixingBowl(
                        color = uiState.bowlColor,
                        modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                            dragAndDropState.dropTargetBounds = layoutCoordinates.boundsInWindow()
                        }
                    )

                    Spacer(modifier = Modifier.weight(0.3f))

                    PaintSelector(viewModel = viewModel, dragAndDropState = dragAndDropState)

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(onClick = { viewModel.clearBowl() }) {
                            Text("Clear Bowl")
                        }
                        Button(onClick = {
                            viewModel.onDone()
                            onDone()
                        }) {
                            Text("Done")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Drag Overlay
        if (dragAndDropState.isDragging && dragAndDropState.draggedPaint != null) {
            Box(modifier = Modifier
                .graphicsLayer {
                    val currentPosition = dragAndDropState.itemStartPosition + dragAndDropState.dragOffset
                    translationX = currentPosition.x
                    translationY = currentPosition.y
                    shadowElevation = 12f
                    scaleX = 1.2f
                    scaleY = 1.2f
                }
                .size(100.dp))
            {
                PaintUI(paint = dragAndDropState.draggedPaint!!)
            }
        }
    }
}

// --- UI COMPONENTS ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSelector(selectedMode: MixingMode, modes: List<MixingMode>, onModeSelected: (MixingMode) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        modes.forEach { mode ->
            ElevatedFilterChip(
                selected = selectedMode == mode,
                onClick = { onModeSelected(mode) },
                label = { Text(mode.title) },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
private fun PaintSelector(viewModel: Module2ViewModel, dragAndDropState: DragAndDropState) {
    val paints = viewModel.getPaintsForCurrentMode()
    val tonePaints = paints.filter { it.type == PaintType.TONE }
    val primaryPaints = paints.filter { it.type == PaintType.PRIMARY }
    val offsetInPx = with(LocalDensity.current) { 50.dp.toPx() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (tonePaints.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                items(tonePaints) { paint ->
                    DraggablePaint(
                        paint = paint,
                        dragState = dragAndDropState,
                        onPaintDropped = { droppedPaint ->
                            val dropPosition = dragAndDropState.itemStartPosition + dragAndDropState.dragOffset + Offset(offsetInPx, offsetInPx)
                            if (dragAndDropState.dropTargetBounds.contains(dropPosition)) {
                                viewModel.onPaintDropped(droppedPaint)
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(primaryPaints) { paint ->
                DraggablePaint(
                    paint = paint,
                    dragState = dragAndDropState,
                    onPaintDropped = { droppedPaint ->
                        val dropPosition = dragAndDropState.itemStartPosition + dragAndDropState.dragOffset + Offset(offsetInPx, offsetInPx)
                        if (dragAndDropState.dropTargetBounds.contains(dropPosition)) {
                            viewModel.onPaintDropped(droppedPaint)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun MixingBowl(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(color)
            .border(2.dp, Color.Gray, CircleShape)
    )
}

@Composable
private fun DraggablePaint(
    paint: PaintColor,
    dragState: DragAndDropState,
    onPaintDropped: (PaintColor) -> Unit
) {
    val isBeingDragged = paint == dragState.draggedPaint
    var currentPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .size(100.dp)
            .onGloballyPositioned { layoutCoordinates ->
                currentPosition = layoutCoordinates.positionInWindow()
            }
            .pointerInput(paint) {
                detectDragGestures(
                    onDragStart = { _ ->
                        dragState.isDragging = true
                        dragState.itemStartPosition = currentPosition
                        dragState.draggedPaint = paint
                    },
                    onDragEnd = {
                        if (dragState.isDragging) {
                            onPaintDropped(paint)
                        }
                        dragState.isDragging = false
                        dragState.dragOffset = Offset.Zero
                        dragState.draggedPaint = null
                    },
                    onDragCancel = {
                        dragState.isDragging = false
                        dragState.dragOffset = Offset.Zero
                        dragState.draggedPaint = null
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragState.dragOffset += dragAmount
                    }
                )
            }
    ) {
        // The original item becomes transparent while it is being dragged
        PaintUI(paint = paint, modifier = Modifier.graphicsLayer {
            alpha = if (isBeingDragged) 0f else 1f
        })
    }
}

@Composable
fun PaintUI(paint: PaintColor, modifier: Modifier = Modifier) {
    var baseModifier = modifier
        .fillMaxSize()
        .padding(8.dp)
        .clip(CircleShape)
        .background(paint.color)

    if (paint.name == "White") {
        baseModifier = baseModifier.border(1.dp, Color.Black, CircleShape)
    }

    Box(
        modifier = baseModifier,
        contentAlignment = Alignment.Center
    ) {
        if (paint.type == PaintType.PRIMARY || paint.name == "White" || paint.name == "Black") {
            val textColor = if (paint.name == "Yellow" || paint.name == "White") Color.Black else Color.White
            Text(paint.name, color = textColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Module2ScreenPreview() {
    ColorTeachingAidsTheme {
        Module2Screen(onDone = {})
    }
}
