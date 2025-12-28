package com.cinescope.cinescope.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinescope.cinescope.presentation.theme.CineScopeTheme

/**
 * iOS-style statistics display component.
 * Shows a value (large, bold) with a label (small, gray) below.
 *
 * Design characteristics:
 * - Value in displayMedium (28sp, Bold), AppleBlue
 * - Label in bodySmall (15sp, Regular), gray
 * - Vertical column, centered alignment
 * - Optional gradient background
 *
 * @param value The statistic value to display (e.g., "127", "4.2")
 * @param label The statistic label (e.g., "Movies", "Avg Rating")
 * @param modifier Modifier for customization
 * @param gradient Optional gradient background brush
 */
@Composable
fun StatsItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    gradient: Brush? = null
) {
    val backgroundModifier = if (gradient != null) {
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(gradient)
            .padding(20.dp)
    } else {
        Modifier
    }

    Column(
        modifier = modifier.then(backgroundModifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (gradient != null) {
                Color.White
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = if (gradient != null) {
                Color.White.copy(alpha = 0.9f)
            } else {
                CineScopeTheme.colors.textSecondary
            }
        )
    }
}
