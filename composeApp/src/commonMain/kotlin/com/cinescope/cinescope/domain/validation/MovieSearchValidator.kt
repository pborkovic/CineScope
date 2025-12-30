package com.cinescope.cinescope.domain.validation

import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result

/**
 * Validator for movie search queries.
 *
 * Business Rules:
 * - Query must not be empty or blank
 * - Query must be at least 2 characters
 * - Query must not exceed 100 characters
 * - Query must contain only valid characters (alphanumeric, spaces, common punctuation)
 */
class MovieSearchValidator : Validator<String> {

    companion object {
        const val MIN_QUERY_LENGTH = 2
        const val MAX_QUERY_LENGTH = 100
        private val VALID_QUERY_REGEX = Regex("^[a-zA-Z0-9\\s.,!?'\\-:&]+$")
    }

    override fun validate(input: String): Result<Unit> {
        val trimmed = input.trim()

        return when {
            trimmed.isEmpty() ->
                Result.Error(NetworkError.Validation.SearchQueryEmpty)

            trimmed.length < MIN_QUERY_LENGTH ->
                Result.Error(NetworkError.Validation.SearchQueryTooShort)

            trimmed.length > MAX_QUERY_LENGTH ->
                Result.Error(NetworkError.Validation.SearchQueryTooLong)

            !VALID_QUERY_REGEX.matches(trimmed) ->
                Result.Error(NetworkError.Validation.SearchQueryInvalidCharacters(trimmed))

            else ->
                Result.Success(Unit)
        }
    }
}
