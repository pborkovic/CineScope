package com.cinescope.cinescope.domain.usecase.watchlist

import com.cinescope.cinescope.domain.repository.WatchlistRepository
import com.cinescope.cinescope.domain.usecase.base.BaseUseCase
import com.cinescope.cinescope.domain.util.Result

/**
 * Use case for removing an item from the watchlist.
 *
 * Simple idempotent operation - succeeds even if item isn't in watchlist.
 * No validation needed beyond the TMDB ID and content type.
 */
class RemoveFromWatchlistUseCase(
    private val watchlistRepository: WatchlistRepository
) : BaseUseCase<RemoveFromWatchlistUseCase.Params, Unit>() {

    data class Params(
        val tmdbId: Int,
        val contentType: String
    )

    override suspend fun execute(params: Params): Result<Unit> {
        return watchlistRepository.removeFromWatchlist(params.tmdbId, params.contentType)
    }
}
