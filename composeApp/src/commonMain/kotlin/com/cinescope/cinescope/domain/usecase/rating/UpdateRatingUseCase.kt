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
 * Use case for updating an existing movie rating.
 *
 * Orchestrates:
 * 1. Rating value validation (0.0-5.0, 0.5 increments)
 * 2. Review validation (optional, max 1000 chars, no spam)
 * 3. Existing rating verification
 * 4. Rating update with new timestamp
 *
 * Business Rules:
 * - Rating must exist before it can be updated
 * - All validation rules apply as in AddRatingUseCase
 * - updatedAt timestamp is automatically set to current time
 */
@OptIn(kotlin.time.ExperimentalTime::class)
class UpdateRatingUseCase(
    private val ratingRepository: RatingRepository,
    private val ratingValidator: RatingValidator
) : BaseUseCase<UpdateRatingUseCase.Params, Unit>() {

    data class Params(
        val ratingId: Long,
        val movieId: Long,
        val rating: Double,
        val review: String? = null
    )

    override suspend fun execute(params: Params): Result<Unit> {
        val validationResult = ratingValidator.validateRatingWithReview(
            rating = params.rating,
            review = params.review
        )
        if (validationResult is Result.Error) {
            return validationResult
        }

        val existingRatings = ratingRepository.getRatingsByMovieId(params.movieId).first()
        val existingRating = existingRatings.firstOrNull { it.id == params.ratingId }

        if (existingRating == null) {
            return Result.Error(
                NetworkError.NotFound("Rating not found")
            )
        }

        val updatedRating = existingRating.copy(
            rating = params.rating,
            review = params.review?.takeIf { it.isNotBlank() },
            updatedAt = TimeProvider.now()
        )

        return ratingRepository.updateRating(updatedRating)
    }
}
