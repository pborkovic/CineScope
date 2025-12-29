package com.cinescope.cinescope.domain.usecase.rating

import com.cinescope.cinescope.domain.model.Rating
import com.cinescope.cinescope.domain.repository.RatingRepository
import com.cinescope.cinescope.domain.usecase.base.BaseUseCase
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.domain.util.TimeProvider
import com.cinescope.cinescope.domain.validation.RatingValidator
import kotlinx.coroutines.flow.first

/**
 * Use case for adding a new movie rating.
 *
 * Orchestrates:
 * 1. Rating value validation (0.0-5.0, 0.5 increments)
 * 2. Review validation (optional, max 1000 chars, no spam)
 * 3. Duplicate rating check (prevents multiple ratings for same movie)
 * 4. Rating persistence
 *
 * Business Rules:
 * - Rating must be between 0.0 and 5.0 in 0.5 increments
 * - Review is optional but must be ≤ 1000 characters if provided
 * - User cannot have multiple ratings for the same movie (use UpdateRatingUseCase instead)
 */
@OptIn(kotlin.time.ExperimentalTime::class)
class AddRatingUseCase(
    private val ratingRepository: RatingRepository,
    private val ratingValidator: RatingValidator
) : BaseUseCase<AddRatingUseCase.Params, Long>() {

    data class Params(
        val movieId: Long,
        val rating: Double,
        val review: String? = null
    )

    override suspend fun execute(params: Params): Result<Long> {
        val validationResult = ratingValidator.validateRatingWithReview(
            rating = params.rating,
            review = params.review
        )

        if (validationResult is Result.Error) {
            return validationResult
        }

        val existingRatings = ratingRepository.getRatingsByMovieId(params.movieId).first()
        if (existingRatings.isNotEmpty()) {
            return Result.Error(
                NetworkError.BadRequest("Rating already exists. Use update instead.")
            )
        }

        val now = TimeProvider.now()
        val rating = Rating(
            id = 0,
            movieId = params.movieId,
            rating = params.rating,
            review = params.review?.takeIf { it.isNotBlank() },
            watchedDate = now,
            createdAt = now,
            updatedAt = now
        )

        return ratingRepository.addRating(rating)
    }
}
