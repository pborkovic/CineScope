package com.cinescope.cinescope.data.mapper

import com.cinescope.cinescope.data.remote.dto.MovieDetailsDto
import com.cinescope.cinescope.data.remote.dto.MovieDto
import com.cinescope.cinescope.domain.model.Movie
import kotlinx.datetime.Clock

object MovieMapper {
    fun MovieDto.toDomain(): Movie {
        return Movie(
            tmdbId = id,
            title = title,
            originalTitle = originalTitle,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            popularity = popularity,
            originalLanguage = originalLanguage,
            adult = adult,
            video = video,
            genreIds = genreIds,
            dateAdded = Clock.System.now()
        )
    }

    fun MovieDetailsDto.toDomain(): Movie {
        return Movie(
            tmdbId = id,
            title = title,
            originalTitle = originalTitle,
            overview = overview,
            posterPath = posterPath,
            backdropPath = backdropPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            popularity = popularity,
            originalLanguage = originalLanguage,
            adult = adult,
            video = video,
            genreIds = genres.map { it.id },
            runtime = runtime,
            budget = budget,
            revenue = revenue,
            status = status,
            tagline = tagline,
            dateAdded = Clock.System.now()
        )
    }
}
