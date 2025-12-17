package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.Rating
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface RatingRepository {
    suspend fun addRating(rating: Rating): Result<Long>

    suspend fun updateRating(rating: Rating): Result<Unit>

    suspend fun deleteRating(id: Long): Result<Unit>

    suspend fun getRatingById(id: Long): Rating?

    suspend fun getRatingsByMovieId(movieId: Long): Flow<List<Rating>>

    suspend fun getAllRatings(): Flow<List<Rating>>

    suspend fun getAverageRating(): Double?

    suspend fun getRatingCount(): Long
}
