package com.cinescope.cinescope

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cinescope.cinescope.di.appModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class CineScopeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                module {
                    single<Context> { this@CineScopeApplication }
                },
                appModule
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}