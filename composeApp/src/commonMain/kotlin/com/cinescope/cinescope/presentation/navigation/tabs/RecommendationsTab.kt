package com.cinescope.cinescope.presentation.navigation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recommend
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.cinescope.cinescope.presentation.navigation.screens.RecommendationsScreen

object RecommendationsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Recommend)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Discover",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(RecommendationsScreen)
    }
}
