package com.cinescope.cinescope.domain.model

import kotlinx.datetime.Instant

/**
 * Represents an item in the user's watchlist for future viewing.
 *
 * The watchlist feature allows users to bookmark movies and TV series they want to watch
 * later. This lightweight data class stores minimal information needed for display in the
 * watchlist UI, with the ability to fetch complete details when needed.
 *
 * @property id Local database identifier (auto-generated, 0 for new entries)
 * @property tmdbId The Movie Database unique identifier for this content
 * @property contentType Content type indicator: "movie" for movies, "tv" for TV series
 * @property title Display title of the content
 * @property posterPath Relative path to the poster image on TMDB servers, if available
 * @property dateAdded Timestamp when this item was added to the watchlist
 *
 * @see Movie
 * @see TVSeries
 */
data class WatchlistItem(
    val id: Long = 0,
    val tmdbId: Int,
    val contentType: String,
    val title: String,
    val posterPath: String? = null,
    val dateAdded: Instant
)
