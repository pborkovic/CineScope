package com.cinescope.cinescope.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Details : Screen("details/{tmdbId}/{contentType}") {
        fun createRoute(tmdbId: Int, contentType: String = "movie") = "details/$tmdbId/$contentType"
    }
    data object Ratings : Screen("ratings")
    data object Recommendations : Screen("recommendations")
    data object Statistics : Screen("statistics")
    data object Watchlist : Screen("watchlist")
    data object Settings : Screen("settings")
}
