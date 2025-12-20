package com.cinescope.cinescope.data.mapper

import com.cinescope.cinescope.data.remote.dto.EpisodeDto
import com.cinescope.cinescope.domain.model.Episode

object EpisodeMapper {
    fun EpisodeDto.toDomain(tvSeriesId: Long): Episode {
        return Episode(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber,
            name = name,
            overview = overview,
            stillPath = stillPath,
            airDate = airDate,
            runtime = runtime,
            voteAverage = voteAverage,
            watched = false
        )
    }
}
