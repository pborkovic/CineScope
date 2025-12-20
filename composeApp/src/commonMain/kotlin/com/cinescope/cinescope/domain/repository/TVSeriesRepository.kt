package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.TVSeries
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface TVSeriesRepository {
    suspend fun searchTVSeries(query: String): Result<List<TVSeries>>

    suspend fun getTVSeriesDetails(tmdbId: Int): Result<TVSeries>

    suspend fun getWatchedTVSeries(): Flow<List<TVSeries>>

    suspend fun getTVSeriesByTmdbId(tmdbId: Int): TVSeries?

    suspend fun saveTVSeries(series: TVSeries): Result<Long>

    suspend fun deleteTVSeries(id: Long): Result<Unit>

    suspend fun getTVSeriesCount(): Long
}
