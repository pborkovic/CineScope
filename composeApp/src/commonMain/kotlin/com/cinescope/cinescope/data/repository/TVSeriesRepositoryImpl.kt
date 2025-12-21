package com.cinescope.cinescope.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cinescope.cinescope.data.local.database.CineScopeDatabase
import com.cinescope.cinescope.data.mapper.TVSeriesMapper.toDomain
import com.cinescope.cinescope.data.remote.api.TmdbApiClient
import com.cinescope.cinescope.data.remote.util.safeApiCall
import com.cinescope.cinescope.domain.model.TVSeries
import com.cinescope.cinescope.domain.repository.TVSeriesRepository
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.domain.util.TimeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(kotlin.time.ExperimentalTime::class)
class TVSeriesRepositoryImpl(
    private val tmdbApiClient: TmdbApiClient,
    private val database: CineScopeDatabase
) : TVSeriesRepository {

    override suspend fun searchTVSeries(query: String): Result<List<TVSeries>> {
        return safeApiCall {
            tmdbApiClient.searchTVSeries(query)
                .let { result ->
                    when (result) {
                        is Result.Success -> result.data.results.map { it.toDomain() }
                        is Result.Error -> throw Exception(result.error.toString())
                        is Result.Loading -> emptyList()
                    }
                }
        }
    }

    override suspend fun getTVSeriesDetails(tmdbId: Int): Result<TVSeries> {
        return safeApiCall {
            tmdbApiClient.getTVSeriesDetails(tmdbId)
                .let { result ->
                    when (result) {
                        is Result.Success -> result.data.toDomain()
                        is Result.Error -> throw Exception(result.error.toString())
                        is Result.Loading -> throw Exception("Loading")
                    }
                }
        }
    }

    override suspend fun getWatchedTVSeries(): Flow<List<TVSeries>> {
        return database.cineScopeDatabaseQueries
            .getAllWatchedTVSeries()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { series ->
                series.map { dbSeries ->
                    TVSeries(
                        id = dbSeries.id,
                        tmdbId = dbSeries.tmdbId.toInt(),
                        name = dbSeries.name,
                        originalName = dbSeries.originalName,
                        overview = dbSeries.overview,
                        posterPath = dbSeries.posterPath,
                        backdropPath = dbSeries.backdropPath,
                        firstAirDate = dbSeries.firstAirDate,
                        lastAirDate = dbSeries.lastAirDate,
                        voteAverage = dbSeries.voteAverage,
                        voteCount = dbSeries.voteCount?.toInt(),
                        popularity = dbSeries.popularity,
                        originalLanguage = dbSeries.originalLanguage,
                        adult = dbSeries.adult == 1L,
                        genreIds = dbSeries.genreIds?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList(),
                        numberOfSeasons = dbSeries.numberOfSeasons?.toInt(),
                        numberOfEpisodes = dbSeries.numberOfEpisodes?.toInt(),
                        status = dbSeries.status,
                        tagline = dbSeries.tagline,
                        type = dbSeries.type,
                        inProduction = dbSeries.inProduction == 1L,
                        dateAdded = kotlinx.datetime.Instant.fromEpochMilliseconds(dbSeries.dateAdded)
                    )
                }
            }
    }

    override suspend fun getTVSeriesByTmdbId(tmdbId: Int): TVSeries? {
        return try {
            val dbSeries = database.cineScopeDatabaseQueries
                .getTVSeriesByTmdbId(tmdbId.toLong())
                .executeAsOneOrNull() ?: return null

            TVSeries(
                id = dbSeries.id,
                tmdbId = dbSeries.tmdbId.toInt(),
                name = dbSeries.name,
                originalName = dbSeries.originalName,
                overview = dbSeries.overview,
                posterPath = dbSeries.posterPath,
                backdropPath = dbSeries.backdropPath,
                firstAirDate = dbSeries.firstAirDate,
                lastAirDate = dbSeries.lastAirDate,
                voteAverage = dbSeries.voteAverage,
                voteCount = dbSeries.voteCount?.toInt(),
                popularity = dbSeries.popularity,
                originalLanguage = dbSeries.originalLanguage,
                adult = dbSeries.adult == 1L,
                genreIds = dbSeries.genreIds?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList(),
                numberOfSeasons = dbSeries.numberOfSeasons?.toInt(),
                numberOfEpisodes = dbSeries.numberOfEpisodes?.toInt(),
                status = dbSeries.status,
                tagline = dbSeries.tagline,
                type = dbSeries.type,
                inProduction = dbSeries.inProduction == 1L,
                dateAdded = kotlinx.datetime.Instant.fromEpochMilliseconds(dbSeries.dateAdded)
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveTVSeries(series: TVSeries): Result<Long> {
        return try {
            database.cineScopeDatabaseQueries.insertTVSeries(
                tmdbId = series.tmdbId.toLong(),
                name = series.name,
                originalName = series.originalName,
                overview = series.overview,
                posterPath = series.posterPath,
                backdropPath = series.backdropPath,
                firstAirDate = series.firstAirDate,
                lastAirDate = series.lastAirDate,
                voteAverage = series.voteAverage,
                voteCount = series.voteCount?.toLong(),
                popularity = series.popularity,
                originalLanguage = series.originalLanguage,
                adult = if (series.adult) 1L else 0L,
                genreIds = series.genreIds.joinToString(","),
                numberOfSeasons = series.numberOfSeasons?.toLong(),
                numberOfEpisodes = series.numberOfEpisodes?.toLong(),
                status = series.status,
                tagline = series.tagline,
                type = series.type,
                inProduction = if (series.inProduction) 1L else 0L,
                dateAdded = TimeProvider.now().toEpochMilliseconds()
            )

            val saved = database.cineScopeDatabaseQueries
                .getTVSeriesByTmdbId(series.tmdbId.toLong())
                .executeAsOne()

            Result.Success(saved.id)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to save TV series"))
        }
    }

    override suspend fun deleteTVSeries(id: Long): Result<Unit> {
        return try {
            database.cineScopeDatabaseQueries.deleteTVSeries(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to delete TV series"))
        }
    }

    override suspend fun getTVSeriesCount(): Long {
        return try {
            database.cineScopeDatabaseQueries.getTVSeriesCount().executeAsOne()
        } catch (e: Exception) {
            0L
        }
    }
}
