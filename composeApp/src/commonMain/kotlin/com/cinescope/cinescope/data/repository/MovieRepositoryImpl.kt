package com.cinescope.cinescope.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.cinescope.cinescope.data.local.database.CineScopeDatabase
import com.cinescope.cinescope.data.mapper.MovieMapper.toDomain
import com.cinescope.cinescope.data.remote.api.TmdbApiClient
import com.cinescope.cinescope.data.remote.util.safeApiCall
import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val tmdbApiClient: TmdbApiClient,
    private val database: CineScopeDatabase
) : MovieRepository {

    override suspend fun searchMovies(query: String): Result<List<Movie>> {
        return safeApiCall {
            tmdbApiClient.searchMovies(query)
                .let { result ->
                    when (result) {
                        is Result.Success -> result.data.results.map { it.toDomain() }
                        is Result.Error -> throw Exception(result.error.toString())
                        is Result.Loading -> emptyList()
                    }
                }
        }
    }

    override suspend fun getMovieDetails(tmdbId: Int): Result<Movie> {
        return safeApiCall {
            tmdbApiClient.getMovieDetails(tmdbId)
                .let { result ->
                    when (result) {
                        is Result.Success -> result.data.toDomain()
                        is Result.Error -> throw Exception(result.error.toString())
                        is Result.Loading -> throw Exception("Loading")
                    }
                }
        }
    }

    override suspend fun getWatchedMovies(): Flow<List<Movie>> {
        return database.cineScopeDatabaseQueries
            .getAllWatchedMovies()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { movies ->
                movies.map { dbMovie ->
                    Movie(
                        id = dbMovie.id,
                        tmdbId = dbMovie.tmdbId.toInt(),
                        title = dbMovie.title,
                        originalTitle = dbMovie.originalTitle,
                        overview = dbMovie.overview,
                        posterPath = dbMovie.posterPath,
                        backdropPath = dbMovie.backdropPath,
                        releaseDate = dbMovie.releaseDate,
                        voteAverage = dbMovie.voteAverage,
                        voteCount = dbMovie.voteCount?.toInt(),
                        popularity = dbMovie.popularity,
                        originalLanguage = dbMovie.originalLanguage,
                        adult = dbMovie.adult == 1L,
                        video = dbMovie.video == 1L,
                        genreIds = dbMovie.genreIds?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList(),
                        runtime = dbMovie.runtime?.toInt(),
                        budget = dbMovie.budget,
                        revenue = dbMovie.revenue,
                        status = dbMovie.status,
                        tagline = dbMovie.tagline,
                        dateAdded = kotlinx.datetime.Instant.fromEpochMilliseconds(dbMovie.dateAdded)
                    )
                }
            }
    }

    override suspend fun getMovieByTmdbId(tmdbId: Int): Movie? {
        return try {
            val dbMovie = database.cineScopeDatabaseQueries
                .getMovieByTmdbId(tmdbId.toLong())
                .executeAsOneOrNull() ?: return null

            Movie(
                id = dbMovie.id,
                tmdbId = dbMovie.tmdbId.toInt(),
                title = dbMovie.title,
                originalTitle = dbMovie.originalTitle,
                overview = dbMovie.overview,
                posterPath = dbMovie.posterPath,
                backdropPath = dbMovie.backdropPath,
                releaseDate = dbMovie.releaseDate,
                voteAverage = dbMovie.voteAverage,
                voteCount = dbMovie.voteCount?.toInt(),
                popularity = dbMovie.popularity,
                originalLanguage = dbMovie.originalLanguage,
                adult = dbMovie.adult == 1L,
                video = dbMovie.video == 1L,
                genreIds = dbMovie.genreIds?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList(),
                runtime = dbMovie.runtime?.toInt(),
                budget = dbMovie.budget,
                revenue = dbMovie.revenue,
                status = dbMovie.status,
                tagline = dbMovie.tagline,
                dateAdded = kotlinx.datetime.Instant.fromEpochMilliseconds(dbMovie.dateAdded)
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveMovie(movie: Movie): Result<Long> {
        return try {
            database.cineScopeDatabaseQueries.insertMovie(
                tmdbId = movie.tmdbId.toLong(),
                title = movie.title,
                originalTitle = movie.originalTitle,
                overview = movie.overview,
                posterPath = movie.posterPath,
                backdropPath = movie.backdropPath,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                voteCount = movie.voteCount?.toLong(),
                popularity = movie.popularity,
                originalLanguage = movie.originalLanguage,
                adult = if (movie.adult) 1L else 0L,
                video = if (movie.video) 1L else 0L,
                genreIds = movie.genreIds.joinToString(","),
                runtime = movie.runtime?.toLong(),
                budget = movie.budget,
                revenue = movie.revenue,
                status = movie.status,
                tagline = movie.tagline,
                dateAdded = Clock.System.now().toEpochMilliseconds()
            )

            val saved = database.cineScopeDatabaseQueries
                .getMovieByTmdbId(movie.tmdbId.toLong())
                .executeAsOne()

            Result.Success(saved.id)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to save movie"))
        }
    }

    override suspend fun deleteMovie(id: Long): Result<Unit> {
        return try {
            database.cineScopeDatabaseQueries.deleteMovie(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to delete movie"))
        }
    }

    override suspend fun getMovieCount(): Long {
        return try {
            database.cineScopeDatabaseQueries.getMovieCount().executeAsOne()
        } catch (e: Exception) {
            0L
        }
    }
}
