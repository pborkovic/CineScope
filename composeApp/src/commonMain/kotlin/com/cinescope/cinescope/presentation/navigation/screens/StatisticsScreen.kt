package com.cinescope.cinescope.presentation.navigation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.cinescope.cinescope.presentation.screens.statistics.StatisticsScreen as StatisticsScreenContent

object StatisticsScreen : Screen {
    @Composable
    override fun Content() {
        StatisticsScreenContent()
    }
}
