package com.cinescope.cinescope.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * iOS-style 8-point grid spacing system.
 * Apple's Human Interface Guidelines recommend using multiples of 8pt for consistent spacing.
 * This creates visual rhythm and makes layouts feel balanced and harmonious.
 */
object Spacing {

    /**
     * Extra small spacing: 4dp
     * Use for: Micro spacing between related elements, icon padding
     */
    val xs: Dp = 4.dp

    /**
     * Small spacing: 8dp
     * Use for: Tight spacing between related items, compact lists
     */
    val sm: Dp = 8.dp

    /**
     * Medium spacing: 16dp (Default)
     * Use for: Standard padding, spacing between components, content margins
     */
    val md: Dp = 16.dp

    /**
     * Large spacing: 24dp
     * Use for: Generous padding, spacing between sections
     */
    val lg: Dp = 24.dp

    /**
     * Extra large spacing: 32dp
     * Use for: Major section breaks, top/bottom screen padding
     */
    val xl: Dp = 32.dp

    /**
     * Extra extra large spacing: 48dp
     * Use for: Large section separators, hero section padding
     */
    val xxl: Dp = 48.dp

    /**
     * Default spacing value (same as md)
     */
    val default: Dp = md
}
