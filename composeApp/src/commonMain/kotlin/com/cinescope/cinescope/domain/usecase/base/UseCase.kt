package com.cinescope.cinescope.domain.usecase.base

import com.cinescope.cinescope.domain.util.Result

/**
 * Base interface for all use cases that transform input parameters into a result.
 *
 * Use cases encapsulate business logic and orchestrate data flow between repositories,
 * ensuring separation of concerns and single responsibility principle.
 *
 * @param P Parameter type (use Unit for no parameters)
 * @param R Return type wrapped in Result
 */
interface UseCase<in P, out R> {
    /**
     * Executes the use case with the given parameters.
     *
     * @param params Input parameters for the use case
     * @return Result containing success data or error
     */
    suspend operator fun invoke(params: P): Result<R>
}
