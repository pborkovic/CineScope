package com.cinescope.cinescope.presentation.navigation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cinescope.cinescope.presentation.screens.home.HomeScreen as HomeScreenContent

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        HomeScreenContent(
            navigateToDetails = { tmdbId ->
                navigator.push(DetailsScreen(tmdbId))
            },
            navigateToSearch = {
                navigator.push(SearchScreen)
            }
        )
    }
}
