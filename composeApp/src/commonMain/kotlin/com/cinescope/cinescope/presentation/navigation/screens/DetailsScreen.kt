package com.cinescope.cinescope.presentation.navigation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cinescope.cinescope.presentation.screens.details.DetailsScreen as DetailsScreenContent

data class DetailsScreen(
    val tmdbId: Int,
    val contentType: String = "movie"
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        DetailsScreenContent(
            tmdbId = tmdbId,
            contentType = contentType,
            navigateBack = {
                navigator.pop()
            }
        )
    }
}
