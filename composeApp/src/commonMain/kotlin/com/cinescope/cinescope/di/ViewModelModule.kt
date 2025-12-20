package com.cinescope.cinescope.di

import com.cinescope.cinescope.presentation.screens.details.DetailsViewModel
import com.cinescope.cinescope.presentation.screens.home.HomeViewModel
import com.cinescope.cinescope.presentation.screens.search.SearchViewModel
import com.cinescope.cinescope.presentation.screens.recommendations.RecommendationsViewModel
import com.cinescope.cinescope.presentation.screens.statistics.StatisticsViewModel
import com.cinescope.cinescope.presentation.screens.watchlist.WatchlistViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::RecommendationsViewModel)
    viewModelOf(::StatisticsViewModel)
    viewModelOf(::WatchlistViewModel)
}
