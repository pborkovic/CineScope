package com.cinescope.cinescope.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cinescope.cinescope.data.local.database.CineScopeDatabase
import com.cinescope.cinescope.domain.model.Rating
import com.cinescope.cinescope.domain.repository.RatingRepository
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.domain.util.TimeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(kotlin.time.ExperimentalTime::class)
class RatingRepositoryImpl(
    private val database: CineScopeDatabase
) : RatingRepository {

    override suspend fun addRating(rating: Rating): Result<Long> {
        return try {
            database.cineScopeDatabaseQueries.insertRating(
                movieId = rating.movieId,
                tvSeriesId = null,
                contentType = "movie",
                rating = rating.rating,
                review = rating.review,
                watchedDate = rating.watchedDate.toEpochMilliseconds(),
                createdAt = TimeProvider.now().toEpochMilliseconds(),
                updatedAt = TimeProvider.now().toEpochMilliseconds()
            )
            Result.Success(rating.id)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to add rating"))
        }
    }

    override suspend fun updateRating(rating: Rating): Result<Unit> {
        return try {
            database.cineScopeDatabaseQueries.updateRating(
                rating = rating.rating,
                review = rating.review,
                updatedAt = TimeProvider.now().toEpochMilliseconds(),
                id = rating.id
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to update rating"))
        }
    }

    override suspend fun deleteRating(id: Long): Result<Unit> {
        return try {
            database.cineScopeDatabaseQueries.deleteRating(id)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to delete rating"))
        }
    }

    override suspend fun getRatingsByMovieId(movieId: Long): Flow<List<Rating>> {
        return database.cineScopeDatabaseQueries
            .getRatingsByMovieId(movieId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { ratings ->
                ratings.map { dbRating ->
                    Rating(
                        id = dbRating.id,
                        movieId = dbRating.movieId ?: 0,
                        rating = dbRating.rating,
                        review = dbRating.review,
                        watchedDate = kotlinx.datetime.Instant.fromEpochMilliseconds(dbRating.watchedDate),
                        createdAt = kotlinx.datetime.Instant.fromEpochMilliseconds(dbRating.createdAt),
                        updatedAt = kotlinx.datetime.Instant.fromEpochMilliseconds(dbRating.updatedAt)
                    )
                }
            }
    }

    override suspend fun getRatingById(id: Long): Rating? {
        return try {
            val dbRating = database.cineScopeDatabaseQueries.getRatingById(id).executeAsOneOrNull() ?: return null
            Rating(
                id = dbRating.id,
                movieId = dbRating.movieId ?: 0,
                rating = dbRating.rating,
                review = dbRating.review,
                watchedDate = kotlinx.datetime.Instant.fromEpochMilliseconds(dbRating.watchedDate),
                createdAt = kotlinx.datetime.Instant.fromEpochMilliseconds(dbRating.createdAt),
                updatedAt = kotlinx.datetime.Instant.fromEpochMilliseconds(dbRating.updatedAt)
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAverageRating(): Double? {
        return try {
            database.cineScopeDatabaseQueries.getAverageRating().executeAsOneOrNull()?.average
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getRatingCount(): Long {
        return try {
            database.cineScopeDatabaseQueries.getRatingCount().executeAsOne()
        } catch (e: Exception) {
            0L
        }
    }

    override suspend fun getAllRatings(): Flow<List<Rating>> {
        return database.cineScopeDatabaseQueries
            .getAllRatings()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { ratings ->
                ratings.map { dbRating ->
                    Rating(
                        id = dbRating.id,
                        movieId = dbRating.movieId ?: 0,
                        rating = dbRating.rating,
                        review = dbRating.review,
                        watchedDate = kotlinx.datetime.Instant.fromEpochMilliseconds(dbRating.watchedDate),
                        createdAt = kotlinx.datetime.Instant.fromEpochMilliseconds(dbRating.createdAt),
                        updatedAt = kotlinx.datetime.Instant.fromEpochMilliseconds(dbRating.updatedAt)
                    )
                }
            }
    }
}
