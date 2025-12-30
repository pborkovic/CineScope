package com.cinescope.cinescope.presentation.navigation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cinescope.cinescope.presentation.screens.search.SearchScreen as SearchScreenContent

object SearchScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        SearchScreenContent(
            navigateToDetails = { tmdbId ->
                navigator.push(DetailsScreen(tmdbId))
            },
            navigateBack = {
                navigator.pop()
            }
        )
    }
}
