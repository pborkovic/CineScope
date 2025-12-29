package com.cinescope.cinescope.domain.usecase.watchlist

import com.cinescope.cinescope.domain.model.WatchlistItem
import com.cinescope.cinescope.domain.repository.WatchlistRepository
import com.cinescope.cinescope.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving the watchlist as a reactive stream.
 *
 * Returns a Flow that automatically emits updated watchlist collections
 * whenever items are added or removed. Perfect for reactive UI updates
 * in the watchlist screen.
 *
 * Items are ordered by date added (most recent first) by the repository.
 *
 * This use case takes no parameters (Unit) and delegates to the repository.
 * While simple, it provides:
 * - Consistent API for ViewModels (all data through use cases)
 * - Easy testing and mocking
 * - Future extensibility (e.g., filtering by content type, sorting options)
 */
class GetWatchlistUseCase(
    private val watchlistRepository: WatchlistRepository
) : FlowUseCase<Unit, List<WatchlistItem>> {
    override suspend fun invoke(params: Unit): Flow<List<WatchlistItem>> {
        return watchlistRepository.getWatchlist()
    }
}
