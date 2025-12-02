package com.juno.colorteachingaids.ui

import androidx.compose.ui.graphics.Color
import com.juno.colorteachingaids.ui.theme.*
import kotlin.math.abs

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
        "Brown" to Color(0xFFA52A2A)
    )

    fun findClosestColor(color: Color): String {
        if (color == Color.LightGray) return "an empty bowl"

        // Find the color name with the smallest distance
        return colorMap.minByOrNull { (_, refColor) -> colorDistance(color, refColor) }?.key ?: "a new color"
    }

    private fun colorDistance(c1: Color, c2: Color): Float {
        val redDiff = c1.red - c2.red
        val greenDiff = c1.green - c2.green
        val blueDiff = c1.blue - c2.blue
        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff
    }
}
