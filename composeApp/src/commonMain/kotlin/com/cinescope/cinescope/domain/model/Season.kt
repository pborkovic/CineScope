package com.cinescope.cinescope.domain.model

data class Season(
    val id: Long = 0,
    val tvSeriesId: Long,
    val seasonNumber: Int,
    val name: String,
    val overview: String? = null,
    val posterPath: String? = null,
    val airDate: String? = null,
    val episodeCount: Int
) {
    fun getPosterUrl(size: String = "w500"): String? {
        return posterPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }
}
