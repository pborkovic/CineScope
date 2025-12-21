package com.cinescope.cinescope.di

import com.cinescope.cinescope.data.repository.*
import com.cinescope.cinescope.domain.recommendation.HybridRecommendationEngine
import com.cinescope.cinescope.domain.recommendation.RecommendationEngine
import com.cinescope.cinescope.domain.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single<MovieRepository> {
        MovieRepositoryImpl(
            tmdbApiClient = get(),
            database = get()
        )
    }

    single<TVSeriesRepository> {
        TVSeriesRepositoryImpl(
            tmdbApiClient = get(),
            database = get()
        )
    }

    single<RatingRepository> {
        RatingRepositoryImpl(database = get())
    }

    single<WatchlistRepository> {
        WatchlistRepositoryImpl(database = get())
    }

    single<RecommendationEngine> {
        HybridRecommendationEngine(
            movieRepository = get(),
            ratingRepository = get()
        )
    }

    single<RecommendationRepository> {
        RecommendationRepositoryImpl(
            recommendationEngine = get()
        )
    }
}
