package com.cinescope.cinescope.presentation.model

/**
 * UI-optimized presentation model for a movie recommendation.
 *
 * This model contains pre-formatted, UI-ready data to eliminate
 * formatting logic from composables and ensure consistent display
 * across the application.
 *
 * All match score calculations, quality level determination, and
 * formatting are performed during mapping from the domain Recommendation model.
 *
 * @property movie UI-optimized movie data
 * @property matchScore Raw match score (0.0 to 1.0)
 * @property matchPercentage Match score as integer percentage (0-100)
 * @property matchDisplay Formatted match display (e.g., "85% Match")
 * @property matchQuality Semantic quality level for UI styling
 * @property reason Human-readable explanation of why this was recommended
 * @property confidenceLevel Confidence indicator (e.g., "High confidence")
 */
data class RecommendationUi(
    val movie: MovieUi,
    val matchScore: Double,
    val matchPercentage: Int,
    val matchDisplay: String,
    val matchQuality: MatchQuality,
    val reason: String,
    val confidenceLevel: String
) {
    companion object {
        const val HIGH_THRESHOLD = 0.75
        const val MEDIUM_THRESHOLD = 0.50
    }

    val isHighMatch: Boolean
        get() = matchScore >= HIGH_THRESHOLD

    val isMediumMatch: Boolean
        get() = matchScore >= MEDIUM_THRESHOLD && matchScore < HIGH_THRESHOLD

    val isLowMatch: Boolean
        get() = matchScore < MEDIUM_THRESHOLD
}

/**
 * Semantic match quality levels for UI styling and messaging.
 *
 * - HIGH: 75-100% match - Strong recommendation
 * - MEDIUM: 50-74% match - Moderate recommendation
 * - LOW: 0-49% match - Weak recommendation
 */
enum class MatchQuality {
    HIGH,
    MEDIUM,
    LOW
}
