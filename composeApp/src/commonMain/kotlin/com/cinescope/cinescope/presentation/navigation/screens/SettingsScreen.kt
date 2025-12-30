package com.cinescope.cinescope.presentation.navigation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.cinescope.cinescope.presentation.screens.settings.SettingsScreen as SettingsScreenContent

object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        SettingsScreenContent()
    }
}
