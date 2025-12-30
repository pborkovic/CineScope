package com.cinescope.cinescope.presentation.mapper

import com.cinescope.cinescope.domain.model.Recommendation
import com.cinescope.cinescope.presentation.mapper.base.PresentationMapper
import com.cinescope.cinescope.presentation.model.MatchQuality
import com.cinescope.cinescope.presentation.model.RecommendationUi

/**
 * Maps Recommendation domain models to RecommendationUi presentation models.
 *
 * Handles all formatting logic including:
 * - Match score conversion (0.0-1.0 → 0-100%)
 * - Match display formatting ("85% Match")
 * - Match quality determination (HIGH/MEDIUM/LOW)
 * - Confidence level labeling
 * - Movie data transformation via MoviePresentationMapper
 *
 * All formatting follows consistent patterns used across the app.
 */
object RecommendationPresentationMapper : PresentationMapper<Recommendation, RecommendationUi> {

    private const val HIGH_THRESHOLD = 0.75
    private const val MEDIUM_THRESHOLD = 0.50

    override fun toPresentation(domain: Recommendation): RecommendationUi {
        val matchPercentage = (domain.matchScore * 100).toInt()

        return RecommendationUi(
            movie = MoviePresentationMapper.toPresentation(domain.movie),
            matchScore = domain.matchScore,
            matchPercentage = matchPercentage,
            matchDisplay = formatMatchDisplay(matchPercentage),
            matchQuality = determineMatchQuality(domain.matchScore),
            reason = domain.reason,
            confidenceLevel = formatConfidenceLevel(domain.matchScore)
        )
    }

    private fun formatMatchDisplay(percentage: Int): String {
        return "$percentage% Match"
    }

    /**
     * Determines match quality based on score thresholds.
     *
     * - HIGH: 75-100% (0.75-1.0)
     * - MEDIUM: 50-74% (0.50-0.74)
     * - LOW: 0-49% (0.0-0.49)
     */
    private fun determineMatchQuality(score: Double): MatchQuality {
        return when {
            score >= HIGH_THRESHOLD -> MatchQuality.HIGH
            score >= MEDIUM_THRESHOLD -> MatchQuality.MEDIUM
            else -> MatchQuality.LOW
        }
    }

    /**
     * Formats confidence level for display.
     *
     * Provides user-friendly labels for match confidence:
     * - 90-100%: "Excellent match"
     * - 75-89%: "Great match"
     * - 60-74%: "Good match"
     * - 50-59%: "Decent match"
     * - Below 50%: "Possible match"
     */
    private fun formatConfidenceLevel(score: Double): String {
        return when {
            score >= 0.90 -> "Excellent match"
            score >= HIGH_THRESHOLD -> "Great match"
            score >= 0.60 -> "Good match"
            score >= MEDIUM_THRESHOLD -> "Decent match"
            else -> "Possible match"
        }
    }
}
