package com.juno.colorteachingaids.ui.student

import android.net.Uri
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.juno.colorteachingaids.ui.theme.ColorTeachingAidsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Module1Screen(
    onDone: () -> Unit,
    viewModel: Module1ViewModel = hiltViewModel()
) {
    val gameState by viewModel.uiState.collectAsState()
    var showHelpDialog by remember { mutableStateOf(false) }
    var showDemoDialog by remember { mutableStateOf(false) }

    if (showHelpDialog) {
        Module1Help(onDismiss = { showHelpDialog = false }, onShowDemo = { showHelpDialog = false; showDemoDialog = true })
    }

    if (showDemoDialog) {
        DemoVideoDialog(videoUri = Uri.parse("asset:///module1demo.webm"), onDismiss = { showDemoDialog = false })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Module 1: Foundations") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onDone(); onDone() }) {
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
        if (gameState.colorItems == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Module1Content(padding, viewModel, gameState, onDone)
        }
    }
}

@Composable
private fun Module1Content(
    padding: PaddingValues,
    viewModel: Module1ViewModel,
    gameState: GameState,
    onDone: () -> Unit
) {
    var rootBounds by remember { mutableStateOf(Rect.Zero) }

    Box(modifier = Modifier.fillMaxSize().padding(padding).onGloballyPositioned { rootBounds = it.boundsInWindow() }) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text("Let's Match Colors!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Drag the colored object to its matching box.", color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))

            // --- Targets ---
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(gameState.colorTargets!!, key = { it.name }) { target ->
                    val matched = gameState.matchedPairs[target.name] == true
                    DropTarget(
                        item = target,
                        matched = matched,
                        onGloballyPositioned = { bounds -> viewModel.updateDropTarget(target, bounds) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Draggables ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val chunkedItems = gameState.colorItems!!.chunked(3)
                chunkedItems.forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        rowItems.forEach { item ->
                            DraggableItem(item = item, viewModel = viewModel) {
                                val isMatched = gameState.matchedPairs[item.name] == true
                                if (isMatched) {
                                    Box(modifier = Modifier.size(80.dp)) // Placeholder
                                } else {
                                    Card(
                                        modifier = Modifier.size(80.dp),
                                        colors = CardDefaults.cardColors(containerColor = item.color)
                                    ) {}
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(onClick = { viewModel.playAgain() }) { Text("Play Again") }
                Spacer(modifier = Modifier.padding(8.dp))
                Button(onClick = { viewModel.onDone(); onDone() }) { Text("Done") }
            }
        }

        // --- Dragged Item Overlay ---
        if (gameState.isDragging) {
            Box(
                modifier = Modifier.graphicsLayer {
                    translationX = gameState.dragPosition.x - rootBounds.left
                    translationY = gameState.dragPosition.y - rootBounds.top
                }
            ) {
                Card(modifier = Modifier.size(80.dp), colors = CardDefaults.cardColors(containerColor = gameState.draggedItem?.color ?: Color.Transparent)) {}
            }
        }
    }
}

@Composable
private fun DraggableItem(
    item: ColorInfo,
    viewModel: Module1ViewModel,
    content: @Composable () -> Unit
) {
    var startPosition by remember { mutableStateOf(Offset.Zero) }
    Box(
        modifier = Modifier
            .onGloballyPositioned { startPosition = it.localToWindow(Offset.Zero) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> viewModel.onDragStart(item, startPosition + offset) },
                    onDragEnd = { viewModel.onDrop() },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        viewModel.onDrag(dragAmount)
                    }
                )
            }
    ) {
        content()
    }
}

@Composable
private fun DropTarget(
    item: ColorInfo,
    matched: Boolean,
    onGloballyPositioned: (Rect) -> Unit
) {
    Box(
        modifier = Modifier.padding(8.dp).onGloballyPositioned { coordinates -> onGloballyPositioned(coordinates.boundsInWindow()) }
    ) {
        if (matched) {
            Card(modifier = Modifier.fillMaxWidth().height(100.dp), colors = CardDefaults.cardColors(containerColor = item.color.copy(alpha = 0.5f))) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("Matched!", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        } else {
            Card(modifier = Modifier.fillMaxWidth().height(100.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(item.name, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Module1ScreenPreview() {
    ColorTeachingAidsTheme {
        Module1Screen(onDone = {})
    }
}
