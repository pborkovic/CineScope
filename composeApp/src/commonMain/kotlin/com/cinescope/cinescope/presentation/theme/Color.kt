package com.cinescope.cinescope.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object CineScopeColors {
    val AppleBlue = Color(0xFF007AFF)
    val AppleBlueDark = Color(0xFF0A84FF)
    val SurfaceLight = Color(0xFFFAFAFA)
    val SurfaceVariantLight = Color(0xFFF2F2F7)
    val OnSurfaceLight = Color(0xFF1C1C1E)
    val OnSurfaceVariantLight = Color(0xFF48484A)
    val SurfaceDark = Color(0xFF1C1C1E)
    val SurfaceVariantDark = Color(0xFF2C2C2E)
    val OnSurfaceDark = Color(0xFFF2F2F7)
    val OnSurfaceVariantDark = Color(0xFFAEAEB2)
    val GlassLight = Color(0xF0F9F9F9)
    val GlassElevatedLight = Color(0xF5FFFFFF)
    val GlassBorderLight = Color(0x33000000)
    val GlassDark = Color(0xF01C1C1E)
    val GlassElevatedDark = Color(0xF52C2C2E)
    val GlassBorderDark = Color(0x33FFFFFF)
    val SuccessLight = Color(0xFF34C759)
    val WarningLight = Color(0xFFFF9500)
    val ErrorLight = Color(0xFFFF3B30)
    val SuccessDark = Color(0xFF30D158)
    val WarningDark = Color(0xFFFF9F0A)
    val ErrorDark = Color(0xFFFF453A)
    val RatingFilled = Color(0xFFFFCC00)
    val RatingFilledDark = Color(0xFFFFD60A)
    val RatingEmpty = Color(0xFFE5E5EA)
    val RatingEmptyDark = Color(0xFF48484A)
    val GradientStart = Color(0xFF667EEA)
    val GradientEnd = Color(0xFF764BA2)

    // Apple-style card and UI colors
    val AppleWhite = Color(0xFFFFFFFF)
    val CineScopeCardBackground = Color(0xFFFAFAFA)
    val AppleDivider = Color(0xFFE5E5E5)
    val AppleTextPrimary = Color(0xFF000000)
    val AppleTextSecondary = Color(0xFF6C6C70)
}

val LightColorScheme = lightColorScheme(
    primary = CineScopeColors.AppleBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),

    secondary = CineScopeColors.GradientStart,
    onSecondary = Color.White,

    surface = CineScopeColors.SurfaceLight,
    onSurface = CineScopeColors.OnSurfaceLight,
    surfaceVariant = CineScopeColors.SurfaceVariantLight,
    onSurfaceVariant = CineScopeColors.OnSurfaceVariantLight,

    background = CineScopeColors.SurfaceLight,
    onBackground = CineScopeColors.OnSurfaceLight,

    error = CineScopeColors.ErrorLight,
    onError = Color.White
)

val DarkColorScheme = darkColorScheme(
    primary = CineScopeColors.AppleBlueDark,
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF004A77),
    onPrimaryContainer = Color(0xFFD1E4FF),

    secondary = CineScopeColors.GradientStart,
    onSecondary = Color.White,

    surface = CineScopeColors.SurfaceDark,
    onSurface = CineScopeColors.OnSurfaceDark,
    surfaceVariant = CineScopeColors.SurfaceVariantDark,
    onSurfaceVariant = CineScopeColors.OnSurfaceVariantDark,

    background = CineScopeColors.SurfaceDark,
    onBackground = CineScopeColors.OnSurfaceDark,

    error = CineScopeColors.ErrorDark,
    onError = Color.White
)
