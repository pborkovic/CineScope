package com.cinescope.cinescope.domain.validation

import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result

/**
 * Validator for movie/TV series ratings.
 *
 * Business Rules:
 * - Rating must be between 0.0 and 5.0
 * - Rating must be in 0.5 increments (0.0, 0.5, 1.0, 1.5, ..., 5.0)
 * - Review (if provided) must not exceed 1000 characters
 * - Review must not contain spam or offensive content
 */
class RatingValidator : Validator<Double> {

    companion object {
        const val MIN_RATING = 0.0
        const val MAX_RATING = 5.0
        const val RATING_INCREMENT = 0.5
        const val MAX_REVIEW_LENGTH = 1000

        private val SPAM_KEYWORDS = listOf(
            "casino", "click here", "buy now", "limited offer"
        )
    }

    override fun validate(input: Double): Result<Unit> {
        return validateRatingValue(input)
    }

    /**
     * Validates a rating value.
     *
     * @param rating The rating value to validate (0.0 to 5.0)
     * @return Result.Success if valid, Result.Error with typed ValidationError if invalid
     */
    fun validateRatingValue(rating: Double): Result<Unit> {
        return when {
            rating < MIN_RATING ->
                Result.Error(NetworkError.Validation.RatingTooLow)

            rating > MAX_RATING ->
                Result.Error(NetworkError.Validation.RatingTooHigh)

            rating % RATING_INCREMENT != 0.0 ->
                Result.Error(NetworkError.Validation.RatingInvalidIncrement)

            else ->
                Result.Success(Unit)
        }
    }

    /**
     * Validates a review text.
     *
     * @param review The review text to validate
     * @return Result.Success if valid, Result.Error with typed ValidationError if invalid
     */
    fun validateReview(review: String): Result<Unit> {
        return when {
            review.isBlank() ->
                Result.Success(Unit)

            review.length > MAX_REVIEW_LENGTH ->
                Result.Error(NetworkError.Validation.ReviewTooLong)

            containsSpam(review) ->
                Result.Error(NetworkError.Validation.ReviewInvalidContent("Spam or promotional content detected"))

            else ->
                Result.Success(Unit)
        }
    }

    /**
     * Validates both rating value and review together.
     *
     * @param rating The rating value
     * @param review The review text (can be null or empty)
     * @return Result.Success if both are valid, Result.Error with the first validation error found
     */
    fun validateRatingWithReview(rating: Double, review: String?): Result<Unit> {
        val ratingResult = validateRatingValue(rating)

        if (ratingResult is Result.Error) {
            return ratingResult
        }
        if (!review.isNullOrBlank()) {
            return validateReview(review)
        }

        return Result.Success(Unit)
    }

    private fun containsSpam(review: String): Boolean {
        val lowercaseReview = review.lowercase()
        return SPAM_KEYWORDS.any { keyword ->
            lowercaseReview.contains(keyword)
        }
    }
}
