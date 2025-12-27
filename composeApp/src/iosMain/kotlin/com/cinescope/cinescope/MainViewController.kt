package com.cinescope.cinescope

import androidx.compose.ui.window.ComposeUIViewController
import com.cinescope.cinescope.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}

private fun initKoin() {
    try {
        startKoin {
            modules(appModule)
        }
    } catch (e: Exception) {
        // Koin already started, ignore
    }
}