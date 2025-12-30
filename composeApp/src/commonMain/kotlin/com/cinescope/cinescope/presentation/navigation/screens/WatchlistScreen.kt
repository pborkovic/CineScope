package com.cinescope.cinescope.presentation.navigation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cinescope.cinescope.presentation.screens.watchlist.WatchlistScreen as WatchlistScreenContent

object WatchlistScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        WatchlistScreenContent(
            navigateToDetails = { tmdbId ->
                navigator.push(DetailsScreen(tmdbId))
            }
        )
    }
}
