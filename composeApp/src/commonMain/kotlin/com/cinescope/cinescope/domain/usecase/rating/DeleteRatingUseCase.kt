package com.cinescope.cinescope.domain.usecase.rating

import com.cinescope.cinescope.domain.repository.RatingRepository
import com.cinescope.cinescope.domain.usecase.base.BaseUseCase
import com.cinescope.cinescope.domain.util.Result

/**
 * Use case for deleting a movie rating.
 *
 * Simple operation that removes a rating from the system.
 * No validation needed beyond the rating ID.
 */
class DeleteRatingUseCase(
    private val ratingRepository: RatingRepository
) : BaseUseCase<DeleteRatingUseCase.Params, Unit>() {

    data class Params(val ratingId: Long)

    override suspend fun execute(params: Params): Result<Unit> {
        return ratingRepository.deleteRating(params.ratingId)
    }
}
