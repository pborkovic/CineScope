package com.cinescope.cinescope.presentation.model

/**
 * Presentation model for Movie optimized for UI display.
 *
 * Contains pre-formatted, UI-ready data derived from domain Movie model.
 *
 * Differences from domain model:
 * - Pre-computed image URLs (no need for getPosterUrl() calls in UI)
 * - Formatted release year (not full date)
 * - Formatted rating display (e.g., "8.5/10 ★★★★☆")
 * - Genre names as comma-separated string (not just IDs)
 * - Formatted vote count (e.g., "1.2k votes")
 * - Formatted runtime (e.g., "2h 15m")
 * - Safe defaults for all nullable fields
 */
data class MovieUi(
    val id: Long,
    val tmdbId: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseYear: String,
    val ratingDisplay: String,
    val voteCount: String,
    val genreNames: String,
    val runtime: String,
    val tagline: String,
    val isWatched: Boolean = false,
    val userRating: Double? = null,
    val isInWatchlist: Boolean = false
) {
    val shortTitle: String
        get() = if (title.length > 30) "${title.take(27)}..." else title

    val isHighlyRated: Boolean
        get() = ratingDisplay.contains("/10") &&
                ratingDisplay.substringBefore("/").toDoubleOrNull()?.let { it >= 7.5 } == true

    val hasPoster: Boolean
        get() = !posterUrl.contains("placeholder")
}
