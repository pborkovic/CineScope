package com.cinescope.cinescope.di

import org.koin.dsl.module

/**
 * Koin module for use case dependencies.
 *
 * All use cases are registered as factories (not singletons) to ensure
 * fresh instances for each invocation, preventing state leakage.
 */
val useCaseModule = module {
    factory { com.cinescope.cinescope.domain.usecase.movie.SearchMoviesUseCase(get(), get()) }
    factory { com.cinescope.cinescope.domain.usecase.movie.GetWatchedMoviesUseCase(get()) }
    factory { com.cinescope.cinescope.domain.usecase.rating.AddRatingUseCase(get(), get()) }
    factory { com.cinescope.cinescope.domain.usecase.rating.UpdateRatingUseCase(get(), get()) }
    factory { com.cinescope.cinescope.domain.usecase.rating.DeleteRatingUseCase(get()) }
    factory { com.cinescope.cinescope.domain.usecase.watchlist.AddToWatchlistUseCase(get()) }
    factory { com.cinescope.cinescope.domain.usecase.watchlist.RemoveFromWatchlistUseCase(get()) }
    factory { com.cinescope.cinescope.domain.usecase.watchlist.GetWatchlistUseCase(get()) }
    factory { com.cinescope.cinescope.domain.usecase.recommendation.GetRecommendationsUseCase(get()) }
    factory { com.cinescope.cinescope.domain.usecase.statistics.GetMovieStatisticsUseCase(get(), get(), get(), get()) }
}
