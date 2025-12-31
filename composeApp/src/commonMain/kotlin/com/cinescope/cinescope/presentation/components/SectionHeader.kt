package com.cinescope.cinescope.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinescope.cinescope.presentation.theme.CineScopeTheme

/**
 * iOS-style section header with title and optional action button.
 * Provides consistent section headers across the app.
 *
 * Design characteristics:
 * - Title in headlineLarge (20sp, SemiBold)
 * - Action button in AppleBlue
 * - SpaceBetween arrangement
 * - 16dp horizontal padding
 *
 * @param title Section title text
 * @param actionText Optional action button text (e.g., "See All")
 * @param onActionClick Optional action click handler
 * @param modifier Modifier for customization
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = CineScopeTheme.colors.textPrimary
        )

        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onActionClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
