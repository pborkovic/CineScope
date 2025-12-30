package com.cinescope.cinescope.domain.usecase.movie

import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.usecase.base.BaseUseCase
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.domain.validation.MovieSearchValidator

/**
 * Use case for searching movies by query string.
 *
 * Validates search query and delegates to repository for data retrieval.
 *
 * Business Rules:
 * - Query must be at least 2 characters
 * - Query must not exceed 100 characters
 * - Query is trimmed and normalized before search
 */
class SearchMoviesUseCase(
    private val movieRepository: MovieRepository,
    private val searchValidator: MovieSearchValidator
) : BaseUseCase<SearchMoviesUseCase.Params, List<Movie>>() {

    data class Params(val query: String)

    override suspend fun execute(params: Params): Result<List<Movie>> {
        val validationResult = searchValidator.validate(params.query)

        if (validationResult is Result.Error) {
            return validationResult
        }

        val normalizedQuery = params.query.trim()

        return movieRepository.searchMovies(normalizedQuery)
    }
}
