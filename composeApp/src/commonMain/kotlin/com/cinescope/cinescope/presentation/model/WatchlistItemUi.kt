package com.cinescope.cinescope.presentation.model

/**
 * UI-optimized presentation model for a watchlist item.
 *
 * This model contains pre-formatted, UI-ready data to eliminate
 * formatting logic from composables and ensure consistent display
 * across the application.
 *
 * All date formatting, content type labeling, and URL generation
 * are performed during mapping from the domain WatchlistItem model.
 *
 * @property id Local database identifier
 * @property tmdbId The Movie Database unique identifier
 * @property contentType Raw content type ("movie" or "tv")
 * @property contentTypeDisplay User-friendly content type (e.g., "Movie", "TV Series")
 * @property title Content title
 * @property posterUrl Full URL to poster image (with placeholder fallback)
 * @property dateAddedDisplay Formatted date added (e.g., "Added March 15, 2024")
 * @property relativeDateAdded Human-friendly relative date (e.g., "Added 2 days ago")
 * @property isRecent True if added within the last 7 days
 * @property isMovie True if content type is "movie"
 * @property isTvSeries True if content type is "tv"
 */
data class WatchlistItemUi(
    val id: Long,
    val tmdbId: Int,
    val contentType: String,
    val contentTypeDisplay: String,
    val title: String,
    val posterUrl: String,
    val dateAddedDisplay: String,
    val relativeDateAdded: String,
    val isRecent: Boolean,
    val isMovie: Boolean,
    val isTvSeries: Boolean
) {
    companion object {
        const val RECENT_DAYS_THRESHOLD = 7
    }

    val shortTitle: String
        get() = if (title.length > 40) "${title.take(37)}..." else title

    val newBadge: String?
        get() = if (isRecent) "NEW" else null
}
