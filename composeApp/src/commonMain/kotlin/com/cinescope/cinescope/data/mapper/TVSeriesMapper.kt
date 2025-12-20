package com.cinescope.cinescope.data.mapper

import com.cinescope.cinescope.data.remote.dto.TVSeriesDetailsDto
import com.cinescope.cinescope.data.remote.dto.TVSeriesDto
import com.cinescope.cinescope.domain.model.TVSeries
import kotlinx.datetime.Clock

object TVSeriesMapper {
    fun TVSeriesDto.toDomain(): TVSeries {
        return TVSeries(
            tmdbId = id,
            name = name,
            originalName = originalName,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            firstAirDate = firstAirDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            popularity = popularity,
            originalLanguage = originalLanguage,
            genreIds = genreIds,
            dateAdded = Clock.System.now()
        )
    }

    fun TVSeriesDetailsDto.toDomain(): TVSeries {
        return TVSeries(
            tmdbId = id,
            name = name,
            originalName = originalName,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            firstAirDate = firstAirDate,
            lastAirDate = lastAirDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            popularity = popularity,
            originalLanguage = originalLanguage,
            adult = adult,
            genreIds = genres.map { it.id },
            numberOfSeasons = numberOfSeasons,
            numberOfEpisodes = numberOfEpisodes,
            status = status,
            tagline = tagline,
            type = type,
            inProduction = inProduction,
            dateAdded = Clock.System.now()
        )
    }
}
