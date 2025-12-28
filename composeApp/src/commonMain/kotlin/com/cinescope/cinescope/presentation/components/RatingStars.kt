package com.cinescope.cinescope.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cinescope.cinescope.presentation.theme.CineScopeTheme

@Composable
fun RatingStars(
    rating: Double,
    onRatingChanged: ((Double) -> Unit)? = null,
    modifier: Modifier = Modifier,
    starSize: Dp = 32.dp
) {
    val glassColors = CineScopeTheme.glassColors

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(5) { index ->
            val starValue = (index + 1).toDouble()
            val isFilled = rating >= starValue
            val isHalf = rating >= starValue - 0.5 && rating < starValue

            Icon(
                imageVector = when {
                    isFilled -> Icons.Default.Star
                    isHalf -> Icons.Default.StarHalf
                    else -> Icons.Default.StarBorder
                },
                contentDescription = null,
                tint = if (isFilled || isHalf) {
                    glassColors.rating
                } else {
                    glassColors.ratingEmpty
                },
                modifier = Modifier
                    .size(starSize)
                    .then(
                        if (onRatingChanged != null) {
                            Modifier.clickable {
                                onRatingChanged(starValue)
                            }
                        } else Modifier
                    )
            )
        }
    }
}
