package com.cinescope.cinescope.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.cinescope.cinescope.presentation.components.FloatingTabBar
import com.cinescope.cinescope.presentation.components.TabBarItem
import com.cinescope.cinescope.presentation.navigation.tabs.HomeTab
import com.cinescope.cinescope.presentation.navigation.tabs.RecommendationsTab
import com.cinescope.cinescope.presentation.navigation.tabs.SearchTab
import com.cinescope.cinescope.presentation.navigation.tabs.SettingsTab
import com.cinescope.cinescope.presentation.navigation.tabs.WatchlistTab

@Composable
fun AppNavigation() {
    TabNavigator(HomeTab) {
        Box(modifier = Modifier.fillMaxSize()) {
            CurrentTab()

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                BottomNavigationBar()
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    val tabNavigator = cafe.adriel.voyager.navigator.tab.LocalTabNavigator.current

    val tabBarItems = listOf(
        TabBarItem(
            route = "home",
            icon = Icons.Default.Home,
            label = "Home"
        ),
        TabBarItem(
            route = "search",
            icon = Icons.Default.Search,
            label = "Search"
        ),
        TabBarItem(
            route = "recommendations",
            icon = Icons.Default.Recommend,
            label = "Discover"
        ),
        TabBarItem(
            route = "watchlist",
            icon = Icons.Default.Bookmarks,
            label = "Watchlist"
        ),
        TabBarItem(
            route = "settings",
            icon = Icons.Default.Settings,
            label = "Settings"
        )
    )

    FloatingTabBar(
        items = tabBarItems,
        selectedRoute = when (tabNavigator.current) {
            is HomeTab -> "home"
            is SearchTab -> "search"
            is RecommendationsTab -> "recommendations"
            is WatchlistTab -> "watchlist"
            is SettingsTab -> "settings"
            else -> "home"
        },
        onItemClick = { item ->
            val tab = when (item.route) {
                "home" -> HomeTab
                "search" -> SearchTab
                "recommendations" -> RecommendationsTab
                "watchlist" -> WatchlistTab
                "settings" -> SettingsTab
                else -> HomeTab
            }
            tabNavigator.current = tab
        }
    )
}
