package com.cinescope.cinescope.presentation.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Gradients {

    /**
     * Sunset Gradient - Warm coral red to orange
     * Perfect for: Featured content, action cards
     */
    val Sunset = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF6B6B), // Red
            Color(0xFFFF8E53)  // Orange
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Ocean Gradient - Soft blue to purple
     * Perfect for: Primary cards, hero sections
     */
    val Ocean = Brush.linearGradient(
        colors = listOf(
            Color(0xFF667EEA), // Blue
            Color(0xFF764BA2)  // Purple
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Forest Gradient - Teal to mint green
     * Perfect for: Success states, nature/documentary content
     */
    val Forest = Brush.linearGradient(
        colors = listOf(
            Color(0xFF11998E), // Teal
            Color(0xFF38EF7D)  // Green
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Berry Gradient - Hot pink to vibrant red
     * Perfect for: Romance/drama content, highlights
     */
    val Berry = Brush.linearGradient(
        colors = listOf(
            Color(0xFFEC38BC), // Pink
            Color(0xFFFF006E)  // Red
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Sky Gradient - Light blue to cyan
     * Perfect for: Adventure content, open/airy feeling
     */
    val Sky = Brush.linearGradient(
        colors = listOf(
            Color(0xFF4FACFE), // Blue
            Color(0xFF00F2FE)  // Cyan
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * Lavender Gradient - Light purple to deep purple
     * Perfect for: Mystery/thriller content, premium features
     */
    val Lavender = Brush.linearGradient(
        colors = listOf(
            Color(0xFFA8C0FF), // Purple
            Color(0xFF3F2B96)  // Purple
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    /**
     * All gradients in an indexed list for easy rotation
     */
    private val allGradients = listOf(
        Ocean,
        Sunset,
        Forest,
        Berry,
        Sky,
        Lavender
    )

    /**
     * Get a gradient by index (0-5).
     * Use modulo to ensure index wraps around: `getGradient(movie.id % 6)`
     * This ensures consistent gradient assignment while providing variety.
     *
     * @param index Index from 0-5 (or any int - will be wrapped via modulo)
     * @return The gradient brush at that index
     */
    fun getGradient(index: Int): Brush {
        val safeIndex = index.mod(allGradients.size)

        return allGradients[safeIndex]
    }

    /**
     * Get a random gradient from the collection.
     * Useful for one-time use cases where consistency isn't needed.
     *
     * @return A random gradient brush
     */
    fun getRandomGradient(): Brush {
        return allGradients.random()
    }

    /**
     * Total number of available gradients
     */
    val count: Int = allGradients.size
}
