package com.cinescope.cinescope.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.cinescope.cinescope.presentation.theme.CineScopeTheme

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    elevated: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val glassColors = CineScopeTheme.glassColors
    val backgroundColor = if (elevated) glassColors.surfaceElevated else glassColors.surface

    val cardModifier = modifier
        .clip(shape)
        .border(
            width = 0.5.dp,
            color = glassColors.border,
            shape = shape
        )
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.1f),
                    Color.White.copy(alpha = 0.05f)
                )
            ),
            shape = shape
        )

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = cardModifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            content()
        }
    } else {
        Card(
            modifier = cardModifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            content()
        }
    }
}
