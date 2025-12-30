package com.cinescope.cinescope.presentation.mapper

import com.cinescope.cinescope.domain.model.Rating
import com.cinescope.cinescope.domain.util.TimeProvider
import com.cinescope.cinescope.presentation.mapper.base.PresentationMapper
import com.cinescope.cinescope.presentation.model.RatingUi
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

/**
 * Maps Rating domain models to RatingUi presentation models.
 *
 * Handles all formatting logic including:
 * - Star ratings with visual symbols
 * - Date formatting (absolute and relative)
 * - Review text truncation for previews
 * - Computed UI flags (isRecent, wasEdited)
 *
 * All formatting follows consistent patterns used across the app.
 */
@OptIn(ExperimentalTime::class)
object RatingPresentationMapper : PresentationMapper<Rating, RatingUi> {

    private const val FULL_STAR = "★"
    private const val HALF_STAR = "✭"
    private const val EMPTY_STAR = "☆"
    private const val RECENT_DAYS_THRESHOLD = 7

    override fun toPresentation(domain: Rating): RatingUi {
        val now = TimeProvider.now()

        return RatingUi(
            id = domain.id,
            movieId = domain.movieId,
            rating = domain.rating,
            ratingDisplay = formatRating(domain.rating),
            starsDisplay = formatStars(domain.rating),
            review = domain.review,
            reviewPreview = formatReviewPreview(domain.review),
            hasReview = !domain.review.isNullOrBlank(),
            watchedDateDisplay = formatWatchedDate(domain.watchedDate),
            relativeWatchedDate = formatRelativeDate(domain.watchedDate, now),
            createdAtDisplay = formatDate(domain.createdAt),
            updatedAtDisplay = formatDate(domain.updatedAt),
            isRecent = isRecent(domain.createdAt, now),
            wasEdited = domain.createdAt != domain.updatedAt
        )
    }

    /**
     * Formats rating with stars (e.g., "4.5/5 ★★★★☆").
     */
    private fun formatRating(rating: Double): String {
        val rounded = (rating * 10).toInt() / 10.0
        val stars = formatStars(rating)
        return "$rounded/5 $stars"
    }

    /**
     * Formats stars visual representation (e.g., "★★★★☆").
     *
     * Uses:
     * - ★ for full stars
     * - ✭ for half stars
     * - ☆ for empty stars
     */
    private fun formatStars(rating: Double): String {
        val fullStars = rating.toInt()
        val hasHalfStar = (rating - fullStars) >= 0.5
        val emptyStars = 5 - fullStars - if (hasHalfStar) 1 else 0

        return buildString {
            repeat(fullStars) { append(FULL_STAR) }

            if (hasHalfStar){
                append(HALF_STAR)
            }

            repeat(emptyStars) { append(EMPTY_STAR) }
        }
    }

    /**
     * Truncates review to preview length with ellipsis if needed.
     */
    private fun formatReviewPreview(review: String?): String? {
        if (review.isNullOrBlank()){
            return null
        }

        val trimmed = review.trim()

        return if (trimmed.length > RatingUi.REVIEW_PREVIEW_LENGTH) {
            "${trimmed.take(RatingUi.REVIEW_PREVIEW_LENGTH - 3)}..."
        } else {
            trimmed
        }
    }

    /**
     * Formats watched date as "Watched on March 15, 2024".
     */
    private fun formatWatchedDate(instant: Instant): String {
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = getMonthName(localDateTime.monthNumber)
        val day = localDateTime.dayOfMonth
        val year = localDateTime.year

        return "Watched on $month $day, $year"
    }

    /**
     * Formats any instant as "March 15, 2024".
     */
    private fun formatDate(instant: Instant): String {
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = getMonthName(localDateTime.monthNumber)
        val day = localDateTime.dayOfMonth
        val year = localDateTime.year

        return "$month $day, $year"
    }

    /**
     * Formats relative date as "Watched X days/weeks/months ago".
     */
    private fun formatRelativeDate(instant: Instant, now: Instant): String {
        val duration = now - instant

        return when {
            duration < 1.days -> "Watched today"
            duration < 2.days -> "Watched yesterday"
            duration < 7.days -> "Watched ${duration.inWholeDays} days ago"
            duration < 14.days -> "Watched 1 week ago"
            duration < 30.days -> "Watched ${duration.inWholeDays / 7} weeks ago"
            duration < 60.days -> "Watched 1 month ago"
            duration < 365.days -> "Watched ${duration.inWholeDays / 30} months ago"
            else -> {
                val years = duration.inWholeDays / 365
                if (years == 1L) "Watched 1 year ago" else "Watched $years years ago"
            }
        }
    }

    /**
     * Returns true if instant is within the last 7 days.
     */
    private fun isRecent(instant: Instant, now: Instant): Boolean {
        val duration = now - instant

        return duration < RECENT_DAYS_THRESHOLD.days
    }

    /**
     * Converts month number to month name.
     */
    private fun getMonthName(monthNumber: Int): String {
        return when (monthNumber) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Unknown"
        }
    }
}
