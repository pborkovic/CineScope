package com.cinescope.cinescope.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cinescope.cinescope.presentation.components.FloatingTabBar
import com.cinescope.cinescope.presentation.components.TabBarItem
import com.cinescope.cinescope.presentation.screens.details.DetailsScreen
import com.cinescope.cinescope.presentation.screens.home.HomeScreen
import com.cinescope.cinescope.presentation.screens.search.SearchScreen
import com.cinescope.cinescope.presentation.screens.recommendations.RecommendationsScreen
import com.cinescope.cinescope.presentation.screens.settings.SettingsScreen
import com.cinescope.cinescope.presentation.screens.statistics.StatisticsScreen
import com.cinescope.cinescope.presentation.screens.watchlist.WatchlistScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetails = { tmdbId ->
                        navController.navigate(Screen.Details.createRoute(tmdbId))
                    },
                    navigateToSearch = {
                        navController.navigate(Screen.Search.route)
                    }
                )
            }

            composable(Screen.Search.route) {
                SearchScreen(
                    navigateToDetails = { tmdbId ->
                        navController.navigate(Screen.Details.createRoute(tmdbId))
                    },
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screen.Details.route,
                arguments = listOf(
                    navArgument("tmdbId") { type = NavType.IntType },
                    navArgument("contentType") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val tmdbId = backStackEntry.arguments?.getInt("tmdbId") ?: return@composable
                val contentType = backStackEntry.arguments?.getString("contentType") ?: "movie"

                DetailsScreen(
                    tmdbId = tmdbId,
                    contentType = contentType,
                    navigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Ratings.route) {
                // TODO: Ratings list screen
                EmptyRatingsScreen()
            }

            composable(Screen.Recommendations.route) {
                RecommendationsScreen(
                    navigateToDetails = { tmdbId ->
                        navController.navigate(Screen.Details.createRoute(tmdbId))
                    }
                )
            }

            composable(Screen.Statistics.route) {
                StatisticsScreen()
            }

            composable(Screen.Watchlist.route) {
                WatchlistScreen(
                    navigateToDetails = { tmdbId ->
                        navController.navigate(Screen.Details.createRoute(tmdbId))
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }

        // Floating tab bar overlay
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationBar(navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val tabBarItems = listOf(
        TabBarItem(
            route = Screen.Home.route,
            icon = Icons.Default.Home,
            label = "Home"
        ),
        TabBarItem(
            route = Screen.Search.route,
            icon = Icons.Default.Search,
            label = "Search"
        ),
        TabBarItem(
            route = Screen.Recommendations.route,
            icon = Icons.Default.Recommend,
            label = "Discover"
        ),
        TabBarItem(
            route = Screen.Watchlist.route,
            icon = Icons.Default.Bookmarks,
            label = "Watchlist"
        ),
        TabBarItem(
            route = Screen.Settings.route,
            icon = Icons.Default.Settings,
            label = "Settings"
        )
    )

    FloatingTabBar(
        items = tabBarItems,
        selectedRoute = currentDestination?.route,
        onItemClick = { item ->
            navController.navigate(item.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@Composable
private fun EmptyRatingsScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Ratings coming soon!")
        }
    }
}
