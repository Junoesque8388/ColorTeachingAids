package com.juno.colorteachingaids.ui.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

// A public, shared state holder for drag-and-drop operations.
class DragDropState<T> {
    var isDragging by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var draggedItem: T? by mutableStateOf(null)
    val dropTargets = mutableMapOf<T, Rect>()
}
