package com.cinescope.cinescope.data.mapper

import com.cinescope.cinescope.data.remote.dto.SeasonDetailsDto
import com.cinescope.cinescope.data.remote.dto.SeasonDto
import com.cinescope.cinescope.domain.model.Season

object SeasonMapper {
    fun SeasonDto.toDomain(tvSeriesId: Long): Season {
        return Season(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
            name = name,
            overview = overview,
            posterPath = posterPath,
            airDate = airDate,
            episodeCount = episodeCount
        )
    }

    fun SeasonDetailsDto.toDomain(tvSeriesId: Long): Season {
        return Season(
            tvSeriesId = tvSeriesId,
            seasonNumber = seasonNumber,
            name = name,
            overview = overview,
            posterPath = posterPath,
            airDate = airDate,
            episodeCount = episodes.size
        )
    }
}
