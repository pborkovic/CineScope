package com.cinescope.cinescope.domain.model

import kotlinx.datetime.Instant

data class Rating(
    val id: Long = 0,
    val movieId: Long,
    val rating: Double,
    val review: String? = null,
    val watchedDate: Instant,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    init {
        require(rating in 0.0..5.0) { "Rating must be between 0.0 and 5.0" }
        require(rating % 0.5 == 0.0) { "Rating must be in 0.5 increments" }
    }

    fun getStarCount(): Int = (rating * 2).toInt()
}
