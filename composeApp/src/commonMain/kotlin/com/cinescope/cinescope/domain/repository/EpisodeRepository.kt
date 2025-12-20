package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.Episode
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    suspend fun getEpisodesByTVSeriesAndSeason(tvSeriesId: Long, seasonNumber: Int): Flow<List<Episode>>

    suspend fun markEpisodeAsWatched(id: Long, watched: Boolean): Result<Unit>

    suspend fun saveEpisode(episode: Episode): Result<Long>

    suspend fun getWatchedEpisodesCount(tvSeriesId: Long): Int
}
