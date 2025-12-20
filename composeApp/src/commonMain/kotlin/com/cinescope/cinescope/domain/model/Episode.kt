package com.cinescope.cinescope.domain.model

data class Episode(
    val id: Long = 0,
    val tvSeriesId: Long,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val name: String,
    val overview: String? = null,
    val stillPath: String? = null,
    val airDate: String? = null,
    val runtime: Int? = null,
    val voteAverage: Double? = null,
    val watched: Boolean = false
) {
    fun getStillUrl(size: String = "w500"): String? {
        return stillPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }
}
