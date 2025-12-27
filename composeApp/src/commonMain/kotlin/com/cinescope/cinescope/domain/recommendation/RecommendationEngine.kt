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
@OptIn(kotlin.time.ExperimentalTime::class)
class HybridRecommendationEngine(
    private val movieRepository: MovieRepository,
    private val ratingRepository: RatingRepository
) : RecommendationEngine {

    companion object {
        const val CONTENT_WEIGHT = 0.65  // User preference importance
        const val POPULARITY_WEIGHT = 0.35  // Quality/popularity importance
        const val MIN_RATING_THRESHOLD = 3.5  // Movies rated 3.5+ influence preferences
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

        val watchedMovieIds = userRatings.map { it.movieId.toInt() }.toSet()

        val candidates = getCandidateMovies()

        val recommendations = candidates
            .filter { it.tmdbId !in watchedMovieIds }
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
     * Calculates weighted preference scores for each genre based on:
     * - Average rating for that genre (higher ratings = stronger preference)
     * - Frequency of ratings (more ratings = more confident preference)
     * - Recency of ratings (recent ratings weighted higher)
     *
     * @param ratings All user ratings to analyze
     * @return Map of genre ID to normalized preference score (0.0 to 1.0)
     */
    private suspend fun calculateGenrePreferences(ratings: List<Rating>): Map<Int, Double> {
        val genreScores = mutableMapOf<Int, MutableList<Pair<Double, Long>>>()

        for (rating in ratings) {
            val movie = movieRepository.getMovieByTmdbId(rating.movieId.toInt())
            movie?.genreIds?.forEach { genreId ->
                genreScores.getOrPut(genreId) { mutableListOf() }
                    .add(rating.rating to rating.watchedDate.toEpochMilliseconds())
            }
        }

        return genreScores.mapValues { (_, ratingData) ->
            val avgRating = ratingData.map { it.first }.average()
            val frequency = ratingData.size.toDouble()
            val frequencyBonus = (frequency / ratings.size.toDouble()).coerceIn(0.0, 0.3)

            ((avgRating / 5.0) + frequencyBonus).coerceIn(0.0, 1.0)
        }
    }

    /**
     * Calculates a content-based score indicating how well a movie matches user preferences.
     *
     * Uses sophisticated scoring combining:
     * - Genre alignment with weighted preferences (50%)
     * - Quality threshold (TMDB rating) (30%)
     * - Genre diversity penalty (avoid recommending only one genre) (10%)
     * - Recency factor for newer movies (10%)
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
        // Calculate genre match score with proper weighting
        val genreScores = movie.genreIds.mapNotNull { genrePreferences[it] }

        val genreScore = if (genreScores.isNotEmpty()) {
            // Use max genre score (best matching genre) weighted with average
            val maxGenreScore = genreScores.maxOrNull() ?: 0.0
            val avgGenreScore = genreScores.average()
            (maxGenreScore * 0.6 + avgGenreScore * 0.4)
        } else {
            0.3 // Neutral score for movies without genre match
        }

        // Quality filter - penalize low-rated movies
        val voteAvg = movie.voteAverage ?: 5.0
        val qualityScore = when {
            voteAvg >= 7.5 -> 1.0
            voteAvg >= 6.5 -> 0.8
            voteAvg >= 5.5 -> 0.6
            voteAvg >= 4.5 -> 0.4
            else -> 0.2
        }

        // Vote count factor - prefer movies with more votes (more reliable)
        val voteCount = movie.voteCount ?: 0
        val reliabilityFactor = when {
            voteCount >= 1000 -> 1.0
            voteCount >= 500 -> 0.9
            voteCount >= 100 -> 0.8
            else -> 0.6
        }

        // Recency bonus for recent movies
        val releaseYear = movie.releaseDate?.take(4)?.toIntOrNull() ?: 2000
        val currentYear = 2025
        val yearsDiff = currentYear - releaseYear
        val recencyScore = when {
            yearsDiff <= 2 -> 1.0
            yearsDiff <= 5 -> 0.8
            yearsDiff <= 10 -> 0.6
            else -> 0.4
        }

        // Combine all factors with weights
        return (
            genreScore * 0.45 +
            qualityScore * 0.30 +
            reliabilityFactor * 0.15 +
            recencyScore * 0.10
        ).coerceIn(0.0, 1.0)
    }

    /**
     * Calculates a popularity-based score from TMDB metrics.
     *
     * Uses logarithmic scaling for better distribution across popularity ranges.
     * Combines:
     * - TMDB popularity (logarithmic scale) (40%)
     * - Vote average quality (50%)
     * - Vote count reliability (10%)
     *
     * @param movie The movie to score
     * @return Popularity score from 0.0 to 1.0, where higher indicates more popular/highly-rated
     */
    private fun calculatePopularityScore(movie: Movie): Double {
        // Use logarithmic scale for popularity to handle wide range
        val popularity = movie.popularity ?: 1.0
        val normalizedPopularity = (kotlin.math.ln(popularity + 1.0) / kotlin.math.ln(1001.0)).coerceIn(0.0, 1.0)

        // Vote average on 0-1 scale
        val voteAverage = movie.voteAverage ?: 5.0
        val normalizedVoteAverage = (voteAverage / 10.0).coerceIn(0.0, 1.0)

        // Vote count factor with logarithmic scaling
        val voteCount = movie.voteCount ?: 0
        val voteCountScore = (kotlin.math.ln(voteCount.toDouble() + 1.0) / kotlin.math.ln(10001.0)).coerceIn(0.0, 1.0)

        return (
            normalizedPopularity * 0.40 +
            normalizedVoteAverage * 0.50 +
            voteCountScore * 0.10
        ).coerceIn(0.0, 1.0)
    }

    /**
     * Generates a human-readable explanation for why a movie was recommended.
     *
     * Creates specific, contextual reasons based on:
     * - Match score quality
     * - Genre alignment
     * - Movie rating quality
     * - Popularity
     *
     * @param movie The recommended movie
     * @param genrePreferences User's genre preferences
     * @param score The calculated match score (0.0 to 1.0)
     * @return Human-readable recommendation reason
     */
    private fun generateReason(
        movie: Movie,
        genrePreferences: Map<Int, Double>,
        score: Double
    ): String {
        val matchPercentage = (score * 100).toInt()

        // Find best matching genres
        val matchingGenres = movie.genreIds
            .mapNotNull { genreId -> genrePreferences[genreId]?.let { genreId to it } }
            .sortedByDescending { it.second }
            .take(2)

        val hasStrongGenreMatch = matchingGenres.any { it.second > 0.6 }
        val isHighRated = (movie.voteAverage ?: 0.0) >= 7.5
        val isPopular = (movie.voteCount ?: 0) >= 1000

        return when {
            matchPercentage >= 90 && hasStrongGenreMatch ->
                "Perfect match for your taste in highly-rated films"
            matchPercentage >= 85 && isHighRated ->
                "Critically acclaimed film matching your preferences"
            matchPercentage >= 80 && hasStrongGenreMatch ->
                "Strong match based on genres you love"
            matchPercentage >= 75 && isPopular ->
                "Popular film that aligns with your tastes"
            matchPercentage >= 70 ->
                "Good match for what you typically enjoy"
            matchPercentage >= 60 ->
                "Worth exploring based on your viewing history"
            matchPercentage >= 50 && isHighRated ->
                "Highly-rated film you might discover"
            else ->
                "Trending choice to broaden your horizons"
        }
    }

    /**
     * Retrieves candidate movies for recommendation consideration.
     *
     * Fetches popular and trending movies from TMDB to provide a diverse pool of candidates
     * for the recommendation algorithm. Combines multiple pages of popular movies and
     * trending movies to ensure variety.
     *
     * @return List of candidate movies to evaluate for recommendations
     */
    private suspend fun getCandidateMovies(): List<Movie> {
        val candidates = mutableListOf<Movie>()

        // Fetch popular movies (3 pages for variety)
        for (page in 1..3) {
            when (val result = movieRepository.getPopularMovies(page)) {
                is Result.Success -> candidates.addAll(result.data)
                else -> {} // Continue even if one page fails
            }
        }

        // Fetch trending movies for additional variety
        when (val result = movieRepository.getTrendingMovies()) {
            is Result.Success -> candidates.addAll(result.data)
            else -> {} // Continue even if trending fails
        }

        return candidates.distinctBy { it.tmdbId } // Remove duplicates
    }

    /**
     * Fallback method to retrieve popular movies when insufficient user data is available.
     *
     * Fetches TMDB's current popular movies as safe recommendations for new users.
     * Scores are based on TMDB metrics (rating, popularity, vote count).
     *
     * @param limit Maximum number of popular movies to return
     * @return List of recommendations based on popularity
     */
    private suspend fun getPopularMovies(limit: Int): List<Recommendation> {
        val popularMovies = mutableListOf<Movie>()

        // Fetch popular movies
        when (val result = movieRepository.getPopularMovies(page = 1)) {
            is Result.Success -> popularMovies.addAll(result.data)
            else -> {}
        }

        // Fetch trending movies for variety
        when (val result = movieRepository.getTrendingMovies()) {
            is Result.Success -> popularMovies.addAll(result.data)
            else -> {}
        }

        return popularMovies
            .distinctBy { it.tmdbId }
            .take(limit * 2) // Get extra to have variety after scoring
            .map { movie ->
                // Calculate score based on quality metrics
                val popularityScore = calculatePopularityScore(movie)

                val reason = when {
                    (movie.voteAverage ?: 0.0) >= 8.0 -> "Critically acclaimed and highly popular"
                    (movie.voteAverage ?: 0.0) >= 7.0 -> "Highly rated by viewers worldwide"
                    (movie.voteCount ?: 0) >= 5000 -> "Popular choice among movie enthusiasts"
                    else -> "Trending movie worth discovering"
                }

                Recommendation(
                    movie = movie,
                    matchScore = popularityScore,
                    reason = reason
                )
            }
            .sortedByDescending { it.matchScore }
            .take(limit)
    }
}
