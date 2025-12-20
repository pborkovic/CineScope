package com.cinescope.cinescope.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.filled.Search
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
import com.cinescope.cinescope.presentation.screens.details.DetailsScreen
import com.cinescope.cinescope.presentation.screens.home.HomeScreen
import com.cinescope.cinescope.presentation.screens.search.SearchScreen
import com.cinescope.cinescope.presentation.screens.recommendations.RecommendationsScreen
import com.cinescope.cinescope.presentation.screens.statistics.StatisticsScreen
import com.cinescope.cinescope.presentation.screens.watchlist.WatchlistScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
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
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.Home.route } == true,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.Search.route } == true,
            onClick = {
                navController.navigate(Screen.Search.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Recommend, contentDescription = "Discover") },
            label = { Text("Discover") },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.Recommendations.route } == true,
            onClick = {
                navController.navigate(Screen.Recommendations.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Bookmarks, contentDescription = "Watchlist") },
            label = { Text("Watchlist") },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.Watchlist.route } == true,
            onClick = {
                navController.navigate(Screen.Watchlist.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Analytics, contentDescription = "Stats") },
            label = { Text("Stats") },
            selected = currentDestination?.hierarchy?.any { it.route == Screen.Statistics.route } == true,
            onClick = {
                navController.navigate(Screen.Statistics.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
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
