package com.cinescope.cinescope.domain.model

import kotlinx.datetime.Instant

data class Movie(
    val id: Long = 0,
    val tmdbId: Int,
    val title: String,
    val originalTitle: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val popularity: Double? = null,
    val originalLanguage: String? = null,
    val adult: Boolean = false,
    val video: Boolean = false,
    val genreIds: List<Int> = emptyList(),
    val runtime: Int? = null,
    val budget: Long? = null,
    val revenue: Long? = null,
    val status: String? = null,
    val tagline: String? = null,
    val dateAdded: Instant = Instant.DISTANT_PAST
) {
    fun getPosterUrl(size: String = "w500"): String? {
        return posterPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }

    fun getBackdropUrl(size: String = "w780"): String? {
        return backdropPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }
}
