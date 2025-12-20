package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.Season
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {
    suspend fun getSeasonDetails(tvSeriesId: Long, seasonNumber: Int): Result<Season>

    suspend fun getSeasonsByTVSeriesId(tvSeriesId: Long): Flow<List<Season>>

    suspend fun saveSeason(season: Season): Result<Long>
}
