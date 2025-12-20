package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.Rating
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for user rating data access and management.
 *
 * Manages the persistence and retrieval of user ratings for movies and TV series.
 * Provides CRUD operations along with statistical aggregations for analytics and
 * recommendation generation. All ratings are stored locally in the SQLDelight database.
 *
 * ## Responsibilities
 * - Persist user ratings with validation
 * - Retrieve individual and bulk rating data
 * - Calculate statistical aggregations (average, counts, distribution)
 * - Support reactive updates via Flow for UI
 * - Ensure data integrity with rating constraints
 *
 * ## Rating Constraints
 * - Values must be between 0.0 and 5.0
 * - Must be in 0.5 increments (0.0, 0.5, 1.0, ..., 5.0)
 * - Each movie can only have one rating per user
 *
 * @see com.cinescope.cinescope.data.repository.RatingRepositoryImpl
 * @see Rating
 * @see Result
 */
interface RatingRepository {
    /**
     * Adds a new rating to the database.
     *
     * Creates a new rating entry for a movie or TV series. If a rating already exists
     * for the same content, consider using [updateRating] instead.
     *
     * @param rating The rating to add (must satisfy validation constraints)
     * @return [Result.Success] containing the auto-generated database ID, or [Result.Error] on failure
     *
     * @throws IllegalArgumentException if rating value is invalid (caught and wrapped in Result.Error)
     *
     * @see updateRating
     * @see deleteRating
     */
    suspend fun addRating(rating: Rating): Result<Long>

    /**
     * Updates an existing rating in the database.
     *
     * Modifies the rating value and/or review text for an existing rating entry.
     * The updatedAt timestamp will be automatically set to the current time.
     *
     * @param rating The rating with updated values (id must match existing entry)
     * @return [Result.Success] on successful update, or [Result.Error] if rating doesn't exist or update fails
     *
     * @see addRating
     * @see getRatingById
     */
    suspend fun updateRating(rating: Rating): Result<Unit>

    /**
     * Deletes a rating from the database.
     *
     * Permanently removes the specified rating. This operation cannot be undone.
     *
     * @param id Database identifier of the rating to delete
     * @return [Result.Success] on successful deletion, or [Result.Error] if rating doesn't exist or deletion fails
     *
     * @see addRating
     */
    suspend fun deleteRating(id: Long): Result<Unit>

    /**
     * Retrieves a specific rating by its database ID.
     *
     * Performs a local database lookup for the rating with the specified identifier.
     *
     * @param id Database identifier of the rating
     * @return The rating entity if found, null if no rating exists with that ID
     *
     * @see getRatingsByMovieId
     * @see getAllRatings
     */
    suspend fun getRatingById(id: Long): Rating?

    /**
     * Retrieves all ratings for a specific movie as a reactive stream.
     *
     * Returns a Flow that emits updated rating lists whenever ratings for this movie
     * change. Useful for displaying a movie's rating history or editing existing ratings.
     *
     * @param movieId Database identifier of the movie
     * @return Flow emitting lists of ratings for the movie, ordered by date (most recent first)
     *
     * @see getAllRatings
     * @see getRatingById
     */
    suspend fun getRatingsByMovieId(movieId: Long): Flow<List<Rating>>

    /**
     * Retrieves all user ratings as a reactive stream.
     *
     * Returns a Flow that automatically emits updated rating lists whenever any rating
     * is added, modified, or deleted. This is the primary data source for the
     * recommendation engine and statistics calculations.
     *
     * @return Flow emitting lists of all ratings, ordered by watched date (most recent first)
     *
     * @see getRatingsByMovieId
     * @see getAverageRating
     */
    suspend fun getAllRatings(): Flow<List<Rating>>

    /**
     * Calculates the average of all user ratings.
     *
     * Computes the mean rating value across all movies and TV series the user has rated.
     * Returns null if no ratings exist to distinguish from a 0.0 average.
     *
     * @return Average rating from 0.0 to 5.0, or null if no ratings exist
     *
     * @see getRatingCount
     * @see getAllRatings
     */
    suspend fun getAverageRating(): Double?

    /**
     * Counts the total number of ratings in the database.
     *
     * Returns the count of all rating entries, useful for statistics displays
     * and determining if enough data exists for personalized recommendations
     * (minimum 3 ratings recommended).
     *
     * @return Total number of ratings
     *
     * @see getAverageRating
     * @see getAllRatings
     */
    suspend fun getRatingCount(): Long
}
