package com.cinescope.cinescope.presentation.mapper

import com.cinescope.cinescope.domain.usecase.statistics.MovieStatistics
import com.cinescope.cinescope.presentation.model.GenreStatUi
import com.cinescope.cinescope.presentation.model.RatingBarUi
import com.cinescope.cinescope.presentation.model.StatisticsUi
import kotlin.math.roundToInt

/**
 * Maps MovieStatistics domain models to StatisticsUi presentation models.
 *
 * Handles all formatting logic including:
 * - Number formatting with appropriate labels
 * - Percentage calculations for genres and ratings
 * - Star rating displays
 * - Chart data generation (bar widths, percentages)
 * - Pluralization handling
 *
 * All formatting follows consistent patterns used across the app.
 */
object StatisticsPresentationMapper {

    private const val FULL_STAR = "★"
    private const val EMPTY_STAR = "☆"

    fun toPresentation(domain: MovieStatistics): StatisticsUi {
        val totalContent = domain.totalMovies + domain.totalTVSeries
        val hasData = totalContent > 0

        return StatisticsUi(
            totalMovies = domain.totalMovies,
            totalMoviesDisplay = formatCount(domain.totalMovies, "Movie", "Movies"),
            totalTVSeries = domain.totalTVSeries,
            totalTVSeriesDisplay = formatCount(domain.totalTVSeries, "TV Series", "TV Series"),
            totalContent = totalContent,
            totalContentDisplay = formatCount(totalContent, "Item", "Items"),
            totalRatings = domain.totalRatings,
            totalRatingsDisplay = formatCount(domain.totalRatings, "Rating", "Ratings"),
            averageRating = domain.averageRating,
            averageRatingDisplay = formatAverageRating(domain.averageRating),
            watchlistCount = domain.watchlistCount,
            watchlistCountDisplay = formatWatchlistCount(domain.watchlistCount),
            topGenres = formatGenreStats(domain.topGenres, domain.totalMovies),
            ratingDistribution = formatRatingDistribution(domain.ratingDistribution, domain.totalRatings),
            hasData = hasData
        )
    }

    /**
     * Formats count with singular/plural label.
     * Examples: "1 Movie", "15 Movies", "0 Ratings"
     */
    private fun formatCount(count: Long, singular: String, plural: String): String {
        return "$count ${if (count == 1L) singular else plural}"
    }

    /**
     * Formats average rating with stars.
     * Example: "4.2/5.0 ★★★★☆"
     */
    private fun formatAverageRating(rating: Double): String {
        if (rating == 0.0){
            return "No ratings yet"
        }

        val rounded = (rating * 10).roundToInt() / 10.0
        val fullStars = rating.toInt()
        val emptyStars = 5 - fullStars
        val stars = FULL_STAR.repeat(fullStars) + EMPTY_STAR.repeat(emptyStars)

        return "$rounded/5.0 $stars"
    }

    /**
     * Formats watchlist count.
     * Examples: "12 in Watchlist", "Empty Watchlist"
     */
    private fun formatWatchlistCount(count: Long): String {
        return if (count == 0L) {
            "Empty Watchlist"
        } else {
            "$count in Watchlist"
        }
    }

    /**
     * Formats genre statistics with percentages.
     */
    private fun formatGenreStats(
        genres: List<com.cinescope.cinescope.domain.usecase.statistics.GenreCount>,
        totalMovies: Long
    ): List<GenreStatUi> {
        if (totalMovies == 0L){
            return emptyList()
        }

        return genres.map { genre ->
            val percentage = ((genre.count.toDouble() / totalMovies) * 100).roundToInt()

            GenreStatUi(
                name = genre.name,
                count = genre.count,
                countDisplay = formatCount(genre.count.toLong(), "movie", "movies"),
                percentage = percentage,
                percentageDisplay = "$percentage%"
            )
        }
    }

    /**
     * Formats rating distribution for bar charts.
     *
     * Generates data for all rating levels (0-5) even if some have 0 counts,
     * ensuring consistent chart display.
     */
    private fun formatRatingDistribution(
        distribution: Map<Int, Int>,
        totalRatings: Long
    ): List<RatingBarUi> {
        val maxCount = distribution.values.maxOrNull() ?: 1

        return (0..5).map { rating ->
            val count = distribution[rating] ?: 0
            val percentage = if (totalRatings > 0) {
                ((count.toDouble() / totalRatings) * 100).roundToInt()
            } else {
                0
            }
            val barWidth = if (maxCount > 0) {
                (count.toFloat() / maxCount)
            } else {
                0f
            }

            RatingBarUi(
                rating = rating,
                ratingDisplay = formatRatingStars(rating),
                count = count,
                countDisplay = formatCount(count.toLong(), "rating", "ratings"),
                percentage = percentage,
                barWidthFraction = barWidth
            )
        }
    }

    /**
     * Formats rating value with stars.
     * Examples: "5 ★★★★★", "3 ★★★☆☆", "0 ☆☆☆☆☆"
     */
    private fun formatRatingStars(rating: Int): String {
        val fullStars = FULL_STAR.repeat(rating)
        val emptyStars = EMPTY_STAR.repeat(5 - rating)

        return "$rating $fullStars$emptyStars"
    }
}
