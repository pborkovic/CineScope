package com.cinescope.cinescope.presentation.model

/**
 * UI-optimized presentation model for movie statistics.
 *
 * This model contains pre-formatted, UI-ready statistical data to eliminate
 * formatting logic from composables and ensure consistent display.
 *
 * All number formatting, percentage calculations, and text generation
 * are performed during mapping from the domain MovieStatistics model.
 *
 * @property totalMovies Raw movie count
 * @property totalMoviesDisplay Formatted movie count (e.g., "152 Movies")
 * @property totalTVSeries Raw TV series count
 * @property totalTVSeriesDisplay Formatted TV series count (e.g., "43 TV Series")
 * @property totalContent Total movies + TV series
 * @property totalContentDisplay Formatted total content (e.g., "195 Total")
 * @property totalRatings Raw rating count
 * @property totalRatingsDisplay Formatted rating count (e.g., "89 Ratings")
 * @property averageRating Raw average rating (0.0-5.0)
 * @property averageRatingDisplay Formatted average rating (e.g., "4.2/5.0 ★★★★☆")
 * @property watchlistCount Raw watchlist count
 * @property watchlistCountDisplay Formatted watchlist count (e.g., "12 in Watchlist")
 * @property topGenres List of genre statistics for display
 * @property ratingDistribution Distribution data for charts
 * @property hasData True if user has any content
 */
data class StatisticsUi(
    val totalMovies: Long,
    val totalMoviesDisplay: String,
    val totalTVSeries: Long,
    val totalTVSeriesDisplay: String,
    val totalContent: Long,
    val totalContentDisplay: String,
    val totalRatings: Long,
    val totalRatingsDisplay: String,
    val averageRating: Double,
    val averageRatingDisplay: String,
    val watchlistCount: Long,
    val watchlistCountDisplay: String,
    val topGenres: List<GenreStatUi>,
    val ratingDistribution: List<RatingBarUi>,
    val hasData: Boolean
) {
    /**
     * Returns a summary message for the statistics.
     */
    val summaryMessage: String
        get() = when {
            totalContent == 0L -> "Start tracking your movies and TV series!"
            totalRatings == 0L -> "You've added $totalContent items. Start rating them!"
            else -> "You've watched $totalContent items and rated $totalRatings of them"
        }
}

/**
 * UI-optimized genre statistics for display.
 *
 * @property name Genre name
 * @property count Number of movies in this genre
 * @property countDisplay Formatted count (e.g., "15 movies")
 * @property percentage Percentage of total (0-100)
 * @property percentageDisplay Formatted percentage (e.g., "15%")
 */
data class GenreStatUi(
    val name: String,
    val count: Int,
    val countDisplay: String,
    val percentage: Int,
    val percentageDisplay: String
)

/**
 * UI-optimized rating distribution bar data for charts.
 *
 * @property rating Rating value (0-5)
 * @property ratingDisplay Rating display with stars (e.g., "5 ★★★★★")
 * @property count Number of ratings at this level
 * @property countDisplay Formatted count (e.g., "12 ratings")
 * @property percentage Percentage of total ratings (0-100)
 * @property barWidthFraction Fraction for bar chart (0.0-1.0)
 */
data class RatingBarUi(
    val rating: Int,
    val ratingDisplay: String,
    val count: Int,
    val countDisplay: String,
    val percentage: Int,
    val barWidthFraction: Float
)
