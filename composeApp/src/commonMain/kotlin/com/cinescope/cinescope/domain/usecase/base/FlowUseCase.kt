package com.cinescope.cinescope.domain.usecase.base

import kotlinx.coroutines.flow.Flow

/**
 * Base interface for use cases that return a Flow for reactive data streams.
 *
 * Use this for use cases that need to observe continuous data changes
 * (e.g., database queries, real-time updates).
 *
 * @param P Parameter type (use Unit for no parameters)
 * @param R Return type emitted by the Flow
 */
interface FlowUseCase<in P, out R> {
    /**
     * Executes the use case and returns a Flow of results.
     *
     * The Flow will emit new values whenever the underlying data changes,
     * allowing for reactive UI updates.
     *
     * @param params Input parameters for the use case
     * @return Flow emitting results over time
     */
    suspend operator fun invoke(params: P): Flow<R>
}
