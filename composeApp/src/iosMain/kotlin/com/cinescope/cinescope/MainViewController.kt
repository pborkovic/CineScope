package com.cinescope.cinescope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.cinescope.cinescope.di.appModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

private var koinStarted = false

@Composable
private fun SafeApp() {
    App()
}

private fun initKoin() {
    if (!koinStarted) {
        try {
            startKoin {
                modules(appModule)
            }

            koinStarted = true
        } catch (e: Exception) {
            println("=== Koin error: ${e.message} ===")
            e.printStackTrace()
        }
    } else {
        println("=== Koin already initialized ===")
    }
}

fun MainViewController(): UIViewController {
    initKoin()

    return ComposeUIViewController {
        SafeApp()
    }
}