package com.juno.colorteachingaids.ui.student

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

// State Definitions for Module 1, in a separate file to avoid circular dependencies.

data class ColorInfo(val name: String, val color: Color)

data class GameState(
    val colorItems: List<ColorInfo>? = null,
    val colorTargets: List<ColorInfo>? = null,
    val matchedPairs: Map<String, Boolean> = emptyMap(),
    val soundEffectsEnabled: Boolean = false,
    val hapticFeedbackEnabled: Boolean = false,
    val readAloudEnabled: Boolean = false,
    val draggedItem: ColorInfo? = null,
    val dragPosition: Offset = Offset.Zero,
    val isDragging: Boolean = false
)
