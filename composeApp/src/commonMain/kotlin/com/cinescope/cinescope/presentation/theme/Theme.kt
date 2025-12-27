package com.cinescope.cinescope.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class GlassColors(
    val surface: Color,
    val surfaceElevated: Color,
    val border: Color,
    val rating: Color,
    val ratingEmpty: Color
)

val LocalGlassColors = staticCompositionLocalOf {
    GlassColors(
        surface = CineScopeColors.GlassLight,
        surfaceElevated = CineScopeColors.GlassElevatedLight,
        border = CineScopeColors.GlassBorderLight,
        rating = CineScopeColors.RatingFilled,
        ratingEmpty = CineScopeColors.RatingEmpty
    )
}

data class ThemeColors(
    val cardBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val divider: Color
)

val LocalThemeColors = staticCompositionLocalOf {
    ThemeColors(
        cardBackground = CineScopeColors.AppleWhite,
        textPrimary = CineScopeColors.AppleTextPrimary,
        textSecondary = CineScopeColors.AppleTextSecondary,
        divider = CineScopeColors.AppleDivider
    )
}

@Composable
fun CineScopeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val glassColors = if (darkTheme) {
        GlassColors(
            surface = CineScopeColors.GlassDark,
            surfaceElevated = CineScopeColors.GlassElevatedDark,
            border = CineScopeColors.GlassBorderDark,
            rating = CineScopeColors.RatingFilledDark,
            ratingEmpty = CineScopeColors.RatingEmptyDark
        )
    } else {
        GlassColors(
            surface = CineScopeColors.GlassLight,
            surfaceElevated = CineScopeColors.GlassElevatedLight,
            border = CineScopeColors.GlassBorderLight,
            rating = CineScopeColors.RatingFilled,
            ratingEmpty = CineScopeColors.RatingEmpty
        )
    }

    val themeColors = if (darkTheme) {
        ThemeColors(
            cardBackground = CineScopeColors.SurfaceDark,
            textPrimary = CineScopeColors.OnSurfaceDark,
            textSecondary = CineScopeColors.OnSurfaceVariantDark,
            divider = Color(0xFF38383A)
        )
    } else {
        ThemeColors(
            cardBackground = CineScopeColors.AppleWhite,
            textPrimary = CineScopeColors.AppleTextPrimary,
            textSecondary = CineScopeColors.AppleTextSecondary,
            divider = CineScopeColors.AppleDivider
        )
    }

    CompositionLocalProvider(
        LocalGlassColors provides glassColors,
        LocalThemeColors provides themeColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = CineScopeTypography,
            shapes = CineScopeShapes,
            content = content
        )
    }
}

object CineScopeTheme {
    val glassColors: GlassColors
        @Composable
        get() = LocalGlassColors.current

    val colors: ThemeColors
        @Composable
        get() = LocalThemeColors.current
}
