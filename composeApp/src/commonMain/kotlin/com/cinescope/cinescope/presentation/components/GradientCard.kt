package com.cinescope.cinescope.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cinescope.cinescope.presentation.theme.Gradients
import com.cinescope.cinescope.presentation.theme.Shadows.medium

/**
 * Vibrant gradient card component inspired by iOS design language.
 * Used for hero sections, featured content, and visual highlights.
 *
 * Design characteristics:
 * - Colorful gradient backgrounds
 * - 20dp corner radius (iOS standard)
 * - Medium shadow (4dp) for prominence
 * - White text for contrast
 * - Generous padding (20dp) for breathing room
 *
 * @param modifier Modifier for size and position
 * @param gradient Background gradient brush (defaults to Ocean gradient)
 * @param shape Corner radius shape (defaults to 20dp rounded)
 * @param elevation Shadow elevation (defaults to 4dp)
 * @param contentPadding Internal padding for content (defaults to 20dp)
 * @param onClick Optional click handler
 * @param content Card content (should use white or light colors for text)
 */
@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    gradient: Brush = Gradients.Ocean,
    shape: Shape = RoundedCornerShape(20.dp),
    elevation: Dp = 4.dp,
    contentPadding: Dp = 20.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .medium(shape)
            .clip(shape)
            .background(gradient)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(contentPadding),
        content = content
    )
}
