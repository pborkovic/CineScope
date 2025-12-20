package com.cinescope.cinescope

import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import com.cinescope.cinescope.presentation.theme.CineScopeTheme
import com.cinescope.cinescope.presentation.navigation.AppNavigation

@Composable
@Preview
fun App() {
    KoinContext {
        CineScopeTheme {
            AppNavigation()
        }
    }
}