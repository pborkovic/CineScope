package com.cinescope.cinescope.presentation.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * iOS-style shadow system for creating depth and hierarchy.
 * Apple uses very subtle shadows with low opacity to maintain a clean, minimalist aesthetic.
 */
object Shadows {

    /**
     * Subtle shadow for standard cards and surfaces.
     * - Elevation: 2dp
     * - Alpha: 5% black
     * - Use case: Regular cards, list items, subtle elevation
     */
    fun Modifier.subtle(
        shape: Shape = RectangleShape
    ): Modifier = this.shadow(
        elevation = 2.dp,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = 0.05f),
        spotColor = Color.Black.copy(alpha = 0.05f)
    )

    /**
     * Medium shadow for elevated cards and important elements.
     * - Elevation: 4dp
     * - Alpha: 8% black
     * - Use case: Elevated cards, buttons, floating action buttons
     */
    fun Modifier.medium(
        shape: Shape = RectangleShape
    ): Modifier = this.shadow(
        elevation = 4.dp,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = 0.08f),
        spotColor = Color.Black.copy(alpha = 0.08f)
    )

    /**
     * Strong shadow for modals, dialogs, and high-priority elements.
     * - Elevation: 8dp
     * - Alpha: 12% black
     * - Use case: Modals, bottom sheets, popovers, dialogs
     */
    fun Modifier.strong(
        shape: Shape = RectangleShape
    ): Modifier = this.shadow(
        elevation = 8.dp,
        shape = shape,
        ambientColor = Color.Black.copy(alpha = 0.12f),
        spotColor = Color.Black.copy(alpha = 0.12f)
    )

    /**
     * Custom shadow with specified elevation.
     * Automatically adjusts alpha based on elevation for iOS-appropriate appearance.
     *
     * @param elevation Elevation in dp
     * @param shape Shadow shape
     */
    fun Modifier.custom(
        elevation: Dp,
        shape: Shape = RectangleShape
    ): Modifier {
        val alpha = when {
            elevation <= 2.dp -> 0.05f
            elevation <= 4.dp -> 0.08f
            elevation <= 8.dp -> 0.12f
            else -> 0.15f
        }

        return this.shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = Color.Black.copy(alpha = alpha),
            spotColor = Color.Black.copy(alpha = alpha)
        )
    }
}
