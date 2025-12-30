package com.cinescope.cinescope.presentation.mapper

import com.cinescope.cinescope.domain.model.WatchlistItem
import com.cinescope.cinescope.domain.util.TimeProvider
import com.cinescope.cinescope.presentation.mapper.base.PresentationMapper
import com.cinescope.cinescope.presentation.model.WatchlistItemUi
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

/**
 * Maps WatchlistItem domain models to WatchlistItemUi presentation models.
 *
 * Handles all formatting logic including:
 * - Content type labeling ("movie" → "Movie", "tv" → "TV Series")
 * - Date formatting (absolute and relative)
 * - Poster URL generation with fallbacks
 * - Computed UI flags (isRecent, isMovie, isTvSeries)
 *
 * All formatting follows consistent patterns used across the app.
 */
@OptIn(ExperimentalTime::class)
object WatchlistPresentationMapper : PresentationMapper<WatchlistItem, WatchlistItemUi> {

    private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p"
    private const val PLACEHOLDER_POSTER = "https://via.placeholder.com/500x750?text=No+Poster"
    private const val RECENT_DAYS_THRESHOLD = 7

    override fun toPresentation(domain: WatchlistItem): WatchlistItemUi {
        val now = TimeProvider.now()

        return WatchlistItemUi(
            id = domain.id,
            tmdbId = domain.tmdbId,
            contentType = domain.contentType,
            contentTypeDisplay = formatContentType(domain.contentType),
            title = domain.title,
            posterUrl = generatePosterUrl(domain.posterPath),
            dateAddedDisplay = formatDateAdded(domain.dateAdded),
            relativeDateAdded = formatRelativeDateAdded(domain.dateAdded, now),
            isRecent = isRecent(domain.dateAdded, now),
            isMovie = domain.contentType == "movie",
            isTvSeries = domain.contentType == "tv"
        )
    }

    /**
     * Formats content type to user-friendly label.
     */
    private fun formatContentType(contentType: String): String {
        return when (contentType) {
            "movie" -> "Movie"
            "tv" -> "TV Series"
            else -> contentType.replaceFirstChar { it.uppercase() }
        }
    }

    /**
     * Generates full poster URL with fallback to placeholder.
     */
    private fun generatePosterUrl(posterPath: String?): String {
        return if (!posterPath.isNullOrBlank()) {
            "$TMDB_IMAGE_BASE_URL/w500$posterPath"
        } else {
            PLACEHOLDER_POSTER
        }
    }

    /**
     * Formats date added as "Added March 15, 2024".
     */
    private fun formatDateAdded(instant: Instant): String {
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val month = getMonthName(localDateTime.monthNumber)
        val day = localDateTime.dayOfMonth
        val year = localDateTime.year
        return "Added $month $day, $year"
    }

    /**
     * Formats relative date as "Added X days/weeks/months ago".
     */
    private fun formatRelativeDateAdded(instant: Instant, now: Instant): String {
        val duration = now - instant

        return when {
            duration < 1.days -> "Added today"
            duration < 2.days -> "Added yesterday"
            duration < 7.days -> "Added ${duration.inWholeDays} days ago"
            duration < 14.days -> "Added 1 week ago"
            duration < 30.days -> "Added ${duration.inWholeDays / 7} weeks ago"
            duration < 60.days -> "Added 1 month ago"
            duration < 365.days -> "Added ${duration.inWholeDays / 30} months ago"
            else -> {
                val years = duration.inWholeDays / 365
                if (years == 1L) "Added 1 year ago" else "Added $years years ago"
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
