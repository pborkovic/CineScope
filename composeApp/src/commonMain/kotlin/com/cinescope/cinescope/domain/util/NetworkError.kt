package com.cinescope.cinescope.domain.util

sealed class NetworkError(val message: String) {
    data object NoInternet : NetworkError("No internet connection available")
    data object Timeout : NetworkError("Request timed out")
    data object ServerError : NetworkError("Server error occurred")
    data class NotFound(val resource: String) : NetworkError("$resource not found")
    data class Unauthorized(val reason: String = "Unauthorized access") : NetworkError(reason)
    data class BadRequest(val reason: String = "Bad request") : NetworkError(reason)
    data class Unknown(val error: String = "Unknown error occurred") : NetworkError(error)

    sealed class Validation(message: String) : NetworkError(message) {
        data class InvalidRatingValue(val value: Double) :
            Validation("Rating $value is invalid. Must be between 0.0 and 5.0 in 0.5 increments.")

        data object RatingTooLow :
            Validation("Rating must be at least 0.0")

        data object RatingTooHigh :
            Validation("Rating cannot exceed 5.0")

        data object RatingInvalidIncrement :
            Validation("Rating must be in 0.5 increments (e.g., 3.5, 4.0, 4.5)")

        data object SearchQueryTooShort :
            Validation("Search query must be at least 2 characters")

        data object SearchQueryTooLong :
            Validation("Search query cannot exceed 100 characters")

        data object SearchQueryEmpty :
            Validation("Search query cannot be empty")

        data class SearchQueryInvalidCharacters(val query: String) :
            Validation("Search query contains invalid characters")

        data object ReviewTooLong :
            Validation("Review cannot exceed 1000 characters")

        data class ReviewInvalidContent(val reason: String) :
            Validation("Review contains invalid content: $reason")

        data object InvalidContentType :
            Validation("Content type must be 'movie' or 'tv'")

        data class DuplicateWatchlistEntry(val tmdbId: Int) :
            Validation("Item $tmdbId is already in watchlist")

        data class FieldRequired(val fieldName: String) :
            Validation("$fieldName is required")

        data class FieldTooLong(val fieldName: String, val maxLength: Int) :
            Validation("$fieldName cannot exceed $maxLength characters")

        data class InvalidFormat(val fieldName: String, val expectedFormat: String) :
            Validation("$fieldName must be in format: $expectedFormat")
    }

    companion object {
        fun fromHttpCode(code: Int, message: String? = null): NetworkError {
            return when (code) {
                400 -> BadRequest(message ?: "Bad request")
                401 -> Unauthorized(message ?: "Unauthorized")
                404 -> NotFound(message ?: "Resource")
                in 500..599 -> ServerError
                else -> Unknown(message ?: "HTTP $code")
            }
        }
    }
}
