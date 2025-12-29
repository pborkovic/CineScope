package com.cinescope.cinescope.domain.validation

import com.cinescope.cinescope.domain.util.NetworkError

/**
 * Type alias for validation errors to provide better semantic naming in the validation package.
 *
 * All validation errors are defined as NetworkError.Validation subtypes to maintain
 * compatibility with the existing Result<T> error handling system.
 *
 * Available validation errors:
 * - Rating: InvalidRatingValue, RatingTooLow, RatingTooHigh, RatingInvalidIncrement
 * - Search: SearchQueryTooShort, SearchQueryTooLong, SearchQueryEmpty, SearchQueryInvalidCharacters
 * - Review: ReviewTooLong, ReviewInvalidContent
 * - Watchlist: InvalidContentType, DuplicateWatchlistEntry
 * - Generic: FieldRequired, FieldTooLong, InvalidFormat
 */
typealias ValidationError = NetworkError.Validation
