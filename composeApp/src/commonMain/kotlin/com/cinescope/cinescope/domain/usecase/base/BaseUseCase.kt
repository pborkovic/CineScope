package com.cinescope.cinescope.domain.usecase.base

import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Abstract base class for use cases with automatic dispatcher switching.
 *
 * Executes business logic on the Default dispatcher by default, ensuring
 * proper thread management for repository/database operations.
 *
 * @param dispatcher The coroutine dispatcher to execute the use case on (default: Dispatchers.Default)
 */
abstract class BaseUseCase<in P, out R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : UseCase<P, R> {

    /**
     * Final implementation of invoke that switches to the configured dispatcher
     * and delegates to the execute method.
     */
    final override suspend fun invoke(params: P): Result<R> {
        return withContext(dispatcher) {
            execute(params)
        }
    }

    /**
     * Override this method to implement use case business logic.
     *
     * This method is automatically executed on the configured dispatcher.
     *
     * @param params Input parameters for the use case
     * @return Result containing success data or error
     */
    protected abstract suspend fun execute(params: P): Result<R>
}
