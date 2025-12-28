package com.cinescope.cinescope.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cinescope.composeapp.generated.resources.Res
import cinescope.composeapp.generated.resources.cinescope_icon
import org.jetbrains.compose.resources.painterResource

/**
 * CineScope app icon component using the official icon.png.
 *
 * Features:
 * - Film reel with purple gradient
 * - Film strip in cyan
 * - Magnifying glass with orange gradient
 * - High-quality PNG rendering
 */
@Composable
fun CineScopeIcon(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    showBackground: Boolean = false
) {
    Image(
        painter = painterResource(Res.drawable.cinescope_icon),
        contentDescription = "CineScope Icon",
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit
    )
}
