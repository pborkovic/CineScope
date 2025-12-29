package com.cinescope.cinescope.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cinescope.cinescope.presentation.theme.Shadows.medium
import com.cinescope.cinescope.presentation.theme.Shadows.subtle
import com.cinescope.cinescope.presentation.theme.CineScopeTheme

/**
 * iOS-style card component with clean white background and subtle shadow.
 * Replaces the glass morphism effect with a solid, minimalist design.
 *
 * Design characteristics:
 * - Pure white or custom background
 * - 20dp corner radius (iOS standard for large cards)
 * - Subtle shadow for depth (no border)
 * - Clean, borderless aesthetic
 *
 * @param modifier Modifier for size, padding, etc.
 * @param backgroundColor Card background color (defaults to white)
 * @param shape Corner radius shape (defaults to 20dp rounded)
 * @param elevation Shadow elevation level (defaults to subtle 2dp)
 * @param onClick Optional click handler (makes card clickable)
 * @param content Card content
 */
@Composable
fun CineScopeCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null,
    shape: Shape = RoundedCornerShape(20.dp),
    elevation: Dp = 2.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val cardBackground = backgroundColor ?: CineScopeTheme.colors.cardBackground

    val shadowModifier = when {
        elevation <= 2.dp -> Modifier.subtle(shape)
        elevation <= 4.dp -> Modifier.medium(shape)
        else -> Modifier.medium(shape)
    }

    Box(
        modifier = modifier
            .then(shadowModifier)
            .clip(shape)
            .background(cardBackground)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        content = content
    )
}
