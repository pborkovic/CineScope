package com.cinescope.cinescope.domain.validation

import com.cinescope.cinescope.domain.util.Result

/**
 * Base interface for domain validators.
 *
 * Validators encapsulate business rules and input validation logic.
 * They return typed validation errors for better error handling and
 * user feedback.
 *
 * @param T The type being validated
 */
interface Validator<T> {
    /**
     * Validates the given input according to business rules.
     *
     * @param input The value to validate
     * @return Result.Success(Unit) if valid, Result.Error with ValidationError if invalid
     */
    fun validate(input: T): Result<Unit>
}
