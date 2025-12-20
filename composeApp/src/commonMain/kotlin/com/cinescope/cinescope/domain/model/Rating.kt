package com.cinescope.cinescope.domain.model

import kotlinx.datetime.Instant

/**
 * Represents a user's rating for a movie in the CineScope application.
 *
 * This data class stores user ratings on a 5-star scale with half-star increments,
 * along with optional review text and watch history metadata. Ratings are validated
 * to ensure they fall within the acceptable range and increment pattern.
 *
 * @property id Local database identifier (auto-generated, 0 for new entries)
 * @property movieId Foreign key reference to the rated movie's local database ID
 * @property rating User's rating value (0.0 to 5.0 in 0.5 increments)
 * @property review Optional text review or notes about the movie
 * @property watchedDate Timestamp when the user watched the movie
 * @property createdAt Timestamp when this rating was first created
 * @property updatedAt Timestamp of the last modification to this rating
 *
 * @throws IllegalArgumentException if rating is not between 0.0 and 5.0
 * @throws IllegalArgumentException if rating is not in 0.5 increments
 *
 * @see Movie
 */
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

    /**
     * Converts the decimal rating to a star count for UI display.
     *
     * Since ratings are in 0.5 increments on a 5-star scale, the star count
     * represents half-stars (e.g., 4.5 stars = 9 half-stars).
     *
     * @return Number of half-stars (0 to 10) for visual representation
     */
    fun getStarCount(): Int = (rating * 2).toInt()
}
