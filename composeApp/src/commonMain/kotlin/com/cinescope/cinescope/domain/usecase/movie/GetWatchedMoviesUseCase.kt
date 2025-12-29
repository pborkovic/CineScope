package com.cinescope.cinescope.domain.usecase.movie

import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving watched movies as a reactive stream.
 *
 * Returns a Flow that automatically emits updated lists whenever
 * the watched movies collection changes. Perfect for reactive UI
 * updates in screens displaying the user's library.
 *
 * This use case takes no parameters (Unit) and simply delegates
 * to the repository. While simple, it provides:
 * - Consistent API for ViewModels (all data through use cases)
 * - Easy testing and mocking
 * - Future extensibility (e.g., filtering, sorting logic)
 */
class GetWatchedMoviesUseCase(
    private val movieRepository: MovieRepository
) : FlowUseCase<Unit, List<Movie>> {

    override suspend fun invoke(params: Unit): Flow<List<Movie>> {
        return movieRepository.getWatchedMovies()
    }
}
