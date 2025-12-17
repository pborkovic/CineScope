package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun searchMovies(query: String): Result<List<Movie>>

    suspend fun getMovieDetails(tmdbId: Int): Result<Movie>

    suspend fun getWatchedMovies(): Flow<List<Movie>>

    suspend fun getMovieByTmdbId(tmdbId: Int): Movie?

    suspend fun saveMovie(movie: Movie): Result<Long>

    suspend fun deleteMovie(id: Long): Result<Unit>

    suspend fun getMovieCount(): Long
}
