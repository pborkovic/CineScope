package com.cinescope.cinescope

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.cinescope.cinescope.domain.model.ThemePreference
import com.cinescope.cinescope.domain.repository.UserProfileRepository
import com.cinescope.cinescope.presentation.theme.CineScopeTheme
import com.cinescope.cinescope.presentation.navigation.AppNavigation
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    val userProfileRepository: UserProfileRepository = koinInject()
    var themePreference by remember { mutableStateOf(ThemePreference.SYSTEM) }

    LaunchedEffect(Unit) {
        userProfileRepository.getUserProfile().collect { profile ->
            themePreference = profile.themePreference
        }
    }

    val isDarkTheme = when (themePreference) {
        ThemePreference.SYSTEM -> isSystemInDarkTheme()
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
    }

    CineScopeTheme(darkTheme = isDarkTheme) {
        AppNavigation()
    }
}