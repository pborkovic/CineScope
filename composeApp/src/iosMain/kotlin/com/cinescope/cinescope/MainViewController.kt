package com.cinescope.cinescope

import androidx.compose.ui.window.ComposeUIViewController
import com.cinescope.cinescope.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}

private fun initKoin() {
    if (org.koin.core.context.GlobalContext.getOrNull() == null) {
        startKoin {
            modules(appModule)
        }
    }
}