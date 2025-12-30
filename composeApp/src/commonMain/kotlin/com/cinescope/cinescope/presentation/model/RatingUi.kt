package com.cinescope.cinescope.presentation.model

/**
 * UI-optimized presentation model for a movie rating.
 *
 * This model contains pre-formatted, UI-ready data to eliminate
 * formatting logic from composables and ensure consistent display
 * across the application.
 *
 * All date formatting, star calculations, and text truncation are
 * performed during mapping from the domain Rating model.
 *
 * @property id Local database identifier
 * @property movieId Reference to the rated movie
 * @property rating Raw rating value (0.0 to 5.0)
 * @property ratingDisplay Formatted rating with stars (e.g., "4.5/5 ★★★★☆")
 * @property starsDisplay Visual star representation only (e.g., "★★★★☆")
 * @property review Full review text (null if no review)
 * @property reviewPreview First 100 chars of review for preview (null if no review)
 * @property hasReview Quick check if review exists
 * @property watchedDateDisplay Formatted watch date (e.g., "Watched on March 15, 2024")
 * @property relativeWatchedDate Human-friendly relative date (e.g., "Watched 2 days ago")
 * @property createdAtDisplay Formatted creation date (e.g., "Created March 15, 2024")
 * @property updatedAtDisplay Formatted update date (e.g., "Updated March 16, 2024")
 * @property isRecent True if created within the last 7 days
 * @property wasEdited True if createdAt != updatedAt
 */
data class RatingUi(
    val id: Long,
    val movieId: Long,
    val rating: Double,
    val ratingDisplay: String,
    val starsDisplay: String,
    val review: String?,
    val reviewPreview: String?,
    val hasReview: Boolean,
    val watchedDateDisplay: String,
    val relativeWatchedDate: String,
    val createdAtDisplay: String,
    val updatedAtDisplay: String,
    val isRecent: Boolean,
    val wasEdited: Boolean
) {
    companion object {
        const val REVIEW_PREVIEW_LENGTH = 100
    }

    val isHighRating: Boolean
        get() = rating >= 4.0

    val isLowRating: Boolean
        get() = rating <= 2.0

    val ratingQuality: RatingQuality
        get() = when {
            rating >= 4.5 -> RatingQuality.EXCELLENT
            rating >= 3.5 -> RatingQuality.GOOD
            rating >= 2.5 -> RatingQuality.AVERAGE
            rating >= 1.5 -> RatingQuality.BELOW_AVERAGE
            else -> RatingQuality.POOR
        }
}

/**
 * Semantic rating quality levels for UI styling and messaging.
 */
enum class RatingQuality {
    EXCELLENT,
    GOOD,
    AVERAGE,
    BELOW_AVERAGE,
    POOR
}
