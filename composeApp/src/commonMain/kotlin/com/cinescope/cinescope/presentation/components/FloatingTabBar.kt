package com.cinescope.cinescope.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinescope.cinescope.presentation.theme.Shadows.medium
import com.cinescope.cinescope.presentation.theme.Spacing
import com.cinescope.cinescope.presentation.theme.CineScopeTheme

/**
 * iOS-style floating tab bar with rounded corners and elevation.
 *
 * Design characteristics:
 * - Floating above content with margin from edges
 * - Pill-shaped with large corner radius (28dp)
 * - Medium shadow for depth
 * - Clean white background
 * - Icon + label layout
 * - Smooth animations on selection
 */
@Composable
fun FloatingTabBar(
    items: List<TabBarItem>,
    selectedRoute: String?,
    onItemClick: (TabBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f) // Slightly narrower than full width for floating effect
                .medium(RoundedCornerShape(28.dp))
                .clip(RoundedCornerShape(28.dp))
                .background(CineScopeTheme.colors.cardBackground)
                .padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = selectedRoute == item.route

                AppleTabBarItem(
                    icon = item.icon,
                    label = item.label,
                    isSelected = isSelected,
                    onClick = { onItemClick(item) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Individual tab bar item with icon and label.
 * Features smooth scale animation on selection.
 */
@Composable
private fun AppleTabBarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.95f,
        animationSpec = tween(durationMillis = 200)
    )

    val iconColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        CineScopeTheme.colors.textSecondary
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        CineScopeTheme.colors.textSecondary
    }

    Column(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor
        )
    }
}

/**
 * Data class representing a tab bar item.
 */
data class TabBarItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)
