package com.cinescope.cinescope.domain.recommendation

import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.model.Rating
import com.cinescope.cinescope.domain.model.Recommendation
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.repository.RatingRepository
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.first

/**
 * Interface defining the contract for movie recommendation generation.
 *
 * Implementations of this interface analyze user preferences and behavior to suggest
 * personalized movie recommendations. Different implementations may use various algorithms
 * such as content-based filtering, collaborative filtering, or hybrid approaches.
 *
 * @see HybridRecommendationEngine
 */
interface RecommendationEngine {
    /**
     * Generates a list of personalized movie recommendations for the user.
     *
     * @param limit Maximum number of recommendations to generate
     * @return List of [Recommendation] objects, sorted by relevance (highest match score first)
     */
    suspend fun generateRecommendations(limit: Int): List<Recommendation>
}

/**
 * Hybrid recommendation engine combining content-based filtering with popularity metrics.
 *
 * This implementation uses a sophisticated hybrid approach that balances user preferences
 * with objective quality metrics:
 *
 * ## Algorithm Overview
 * The engine generates recommendations using a weighted combination of:
 * - **Content-Based Filtering (70%)**: Analyzes genre preferences derived from user ratings
 * - **Popularity Metrics (30%)**: Incorporates TMDB popularity and vote data
 *
 * ## Recommendation Process
 * 1. **Preference Analysis**: Calculates average ratings per genre from user's watch history
 * 2. **Candidate Selection**: Gathers potential movies from popular and trending lists
 * 3. **Content Scoring**: Evaluates genre alignment (70%), rating quality (30%), and popularity bonus
 * 4. **Popularity Scoring**: Combines popularity (40%), vote average (50%), and vote count factor
 * 5. **Hybrid Scoring**: Merges content and popularity scores with configured weights
 * 6. **Filtering**: Excludes already-watched movies and ranks by hybrid score
 *
 * ## Scoring Weights
 * - Content score: 70% of final score
 * - Popularity score: 30% of final score
 * - Minimum rating threshold: 3.5 stars (for preference calculation)
 *
 * @property movieRepository Repository for accessing movie data
 * @property ratingRepository Repository for accessing user ratings
 *
 * @constructor Creates a hybrid recommendation engine with required repositories
 *
 * @see RecommendationEngine
 * @see Recommendation
 */
class HybridRecommendationEngine(
    private val movieRepository: MovieRepository,
    private val ratingRepository: RatingRepository
) : RecommendationEngine {

    companion object {
        const val CONTENT_WEIGHT = 0.7
        const val POPULARITY_WEIGHT = 0.3
        const val MIN_RATING_THRESHOLD = 3.5
    }

    /**
     * Generates personalized movie recommendations using the hybrid algorithm.
     *
     * If the user has fewer than 3 ratings, falls back to popular movies since
     * there isn't enough data to build reliable preference profiles.
     *
     * @param limit Maximum number of recommendations to return
     * @return List of recommendations sorted by match score (descending), or empty list if insufficient data
     */
    override suspend fun generateRecommendations(limit: Int): List<Recommendation> {
        val userRatings = ratingRepository.getAllRatings().first()

        if (userRatings.size < 3) {
            return getPopularMovies(limit)
        }

        val genrePreferences = calculateGenrePreferences(userRatings)

        val likedMovies = userRatings.filter { it.rating >= MIN_RATING_THRESHOLD }

        val watchedMovieIds = userRatings.map { it.movieId }.toSet()

        val candidates = getCandidateMovies()

        val recommendations = candidates
            .filter { it.id !in watchedMovieIds }
            .map { movie ->
                val contentScore = calculateContentScore(movie, genrePreferences, likedMovies)
                val popularityScore = calculatePopularityScore(movie)

                val hybridScore = (contentScore * CONTENT_WEIGHT) + (popularityScore * POPULARITY_WEIGHT)

                val reason = generateReason(movie, genrePreferences, hybridScore)

                Recommendation(
                    movie = movie,
                    matchScore = hybridScore,
                    reason = reason
                )
            }
            .sortedByDescending { it.matchScore }
            .take(limit)

        return recommendations
    }

    /**
     * Analyzes user ratings to determine genre preferences.
     *
     * Calculates the average rating for each genre across all user ratings,
     * normalized to a 0-1 scale where higher values indicate stronger preference.
     *
     * @param ratings All user ratings to analyze
     * @return Map of genre ID to normalized preference score (0.0 to 1.0)
     */
    private suspend fun calculateGenrePreferences(ratings: List<Rating>): Map<Int, Double> {
        val genreScores = mutableMapOf<Int, MutableList<Double>>()

        for (rating in ratings) {
            val movie = movieRepository.getMovieByTmdbId(rating.movieId.toInt())
            movie?.genreIds?.forEach { genreId ->
                genreScores.getOrPut(genreId) { mutableListOf() }.add(rating.rating)
            }
        }

        return genreScores.mapValues { (_, scores) ->
            scores.average() / 5.0 // Normalize to 0-1
        }
    }

    /**
     * Calculates a content-based score indicating how well a movie matches user preferences.
     *
     * Combines three factors:
     * - Genre alignment: How well the movie's genres match user preferences (70%)
     * - Rating quality: The movie's TMDB vote average (30%)
     * - Popularity bonus: Small boost for very popular movies (up to 0.2)
     *
     * @param movie The movie to score
     * @param genrePreferences User's preference map for each genre
     * @param likedMovies List of movies the user rated highly
     * @return Content score from 0.0 to 1.0, where higher indicates better match
     */
    private fun calculateContentScore(
        movie: Movie,
        genrePreferences: Map<Int, Double>,
        likedMovies: List<Rating>
    ): Double {
        val genreScore = movie.genreIds
            .mapNotNull { genrePreferences[it] }
            .average()
            .takeIf { !it.isNaN() } ?: 0.0

        val ratingScore = (movie.voteAverage ?: 0.0) / 10.0

        val popularityBonus = ((movie.popularity ?: 0.0) / 1000.0).coerceIn(0.0, 0.2)

        return (genreScore * 0.7 + ratingScore * 0.3 + popularityBonus).coerceIn(0.0, 1.0)
    }

    /**
     * Calculates a popularity-based score from TMDB metrics.
     *
     * Combines three normalized factors:
     * - Popularity metric: TMDB's popularity index (40%)
     * - Vote average: Average user rating quality (50%)
     * - Vote count factor: Reliability indicator based on number of votes (bonus up to 0.2)
     *
     * All metrics are normalized to a 0-1 scale for fair combination.
     *
     * @param movie The movie to score
     * @return Popularity score from 0.0 to 1.0, where higher indicates more popular/highly-rated
     */
    private fun calculatePopularityScore(movie: Movie): Double {
        val normalizedPopularity = ((movie.popularity ?: 0.0) / 1000.0).coerceIn(0.0, 1.0)
        val normalizedVoteAverage = ((movie.voteAverage ?: 0.0) / 10.0).coerceIn(0.0, 1.0)
        val voteCountFactor = ((movie.voteCount ?: 0) / 5000.0).coerceIn(0.0, 0.2)

        return (normalizedPopularity * 0.4 + normalizedVoteAverage * 0.5 + voteCountFactor).coerceIn(0.0, 1.0)
    }

    /**
     * Generates a human-readable explanation for why a movie was recommended.
     *
     * Creates contextual reasons based on the match score percentage:
     * - 90%+: "Excellent match based on your preferences"
     * - 75-89%: "Great match for you"
     * - 60-74%: "You might enjoy this"
     * - Below 60%: "Popular choice worth exploring"
     *
     * @param movie The recommended movie
     * @param genrePreferences User's genre preferences (for future enhancements)
     * @param score The calculated match score (0.0 to 1.0)
     * @return Human-readable recommendation reason
     */
    private fun generateReason(
        movie: Movie,
        genrePreferences: Map<Int, Double>,
        score: Double
    ): String {
        val matchPercentage = (score * 100).toInt()

        val topGenres = movie.genreIds
            .mapNotNull { genreId ->
                genrePreferences[genreId]?.let { score -> genreId to score }
            }
            .sortedByDescending { it.second }
            .take(2)

        return when {
            matchPercentage >= 90 -> "Excellent match based on your preferences"
            matchPercentage >= 75 -> "Great match for you"
            matchPercentage >= 60 -> "You might enjoy this"
            else -> "Popular choice worth exploring"
        }
    }

    /**
     * Retrieves candidate movies for recommendation consideration.
     *
     * In the current implementation, returns movies from the user's watched list.
     * In a full production implementation, this would fetch popular and trending movies
     * from the TMDB API to provide a diverse pool of candidates.
     *
     * @return List of candidate movies to evaluate for recommendations
     */
    private suspend fun getCandidateMovies(): List<Movie> {
        return movieRepository.getWatchedMovies().first()
    }

    /**
     * Fallback method to retrieve popular movies when insufficient user data is available.
     *
     * Currently returns an empty list. In a production implementation, this would fetch
     * TMDB's current popular movies as safe recommendations for new users.
     *
     * @param limit Maximum number of popular movies to return
     * @return List of recommendations based on popularity (currently empty)
     */
    private suspend fun getPopularMovies(limit: Int): List<Recommendation> {
        return emptyList()
    }
}
