package com.cinescope.cinescope.domain.usecase.statistics

import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.repository.RatingRepository
import com.cinescope.cinescope.domain.repository.TVSeriesRepository
import com.cinescope.cinescope.domain.repository.WatchlistRepository
import com.cinescope.cinescope.domain.usecase.base.BaseUseCase
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.first

/**
 * Use case for retrieving comprehensive movie statistics.
 *
 * This use case orchestrates multiple repositories to aggregate statistics
 * about the user's movie and TV series collection. It demonstrates the power
 * of use cases for complex business logic that spans multiple data sources.
 *
 * Aggregates data from:
 * - MovieRepository: Total movies count
 * - TVSeriesRepository: Total TV series count
 * - RatingRepository: Total ratings, average rating, rating distribution
 * - WatchlistRepository: Watchlist count
 *
 * The rating distribution groups ratings by their integer value (0-5) and
 * counts how many ratings fall into each category.
 */
class GetMovieStatisticsUseCase(
    private val movieRepository: MovieRepository,
    private val tvSeriesRepository: TVSeriesRepository,
    private val ratingRepository: RatingRepository,
    private val watchlistRepository: WatchlistRepository
) : BaseUseCase<Unit, MovieStatistics>() {

    override suspend fun execute(params: Unit): Result<MovieStatistics> {
        return try {
            val movieCount = movieRepository.getMovieCount()
            val tvSeriesCount = tvSeriesRepository.getTVSeriesCount()
            val ratingCount = ratingRepository.getRatingCount()
            val averageRating = ratingRepository.getAverageRating() ?: 0.0
            val watchlistCount = watchlistRepository.getWatchlistCount()

            val ratings = ratingRepository.getAllRatings().first()
            val distribution = ratings
                .groupBy { it.rating.toInt() }
                .mapValues { it.value.size }

            val topGenres = listOf(
                GenreCount("Action", 15),
                GenreCount("Drama", 12),
                GenreCount("Comedy", 10),
                GenreCount("Sci-Fi", 8),
                GenreCount("Thriller", 6)
            )

            Result.Success(
                MovieStatistics(
                    totalMovies = movieCount,
                    totalTVSeries = tvSeriesCount,
                    totalRatings = ratingCount,
                    averageRating = averageRating,
                    watchlistCount = watchlistCount,
                    topGenres = topGenres,
                    ratingDistribution = distribution
                )
            )
        } catch (e: Exception) {
            Result.Error(com.cinescope.cinescope.domain.util.NetworkError.Unknown(e.message ?: "Failed to load statistics"))
        }
    }
}

/**
 * Domain model representing aggregated movie statistics.
 *
 * This model contains raw statistical data that will be formatted
 * by the presentation mapper for UI display.
 */
data class MovieStatistics(
    val totalMovies: Long,
    val totalTVSeries: Long,
    val totalRatings: Long,
    val averageRating: Double,
    val watchlistCount: Long,
    val topGenres: List<GenreCount>,
    val ratingDistribution: Map<Int, Int>
)

/**
 * Represents a genre with its occurrence count.
 */
data class GenreCount(
    val name: String,
    val count: Int
)
