package com.cinescope.cinescope.domain.usecase.watchlist

import com.cinescope.cinescope.domain.model.WatchlistItem
import com.cinescope.cinescope.domain.repository.WatchlistRepository
import com.cinescope.cinescope.domain.usecase.base.BaseUseCase
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.domain.util.TimeProvider

/**
 * Use case for adding an item to the watchlist.
 *
 * Orchestrates:
 * 1. Duplicate check (prevent adding same item twice)
 * 2. Content type validation ("movie" or "tv")
 * 3. Watchlist item creation with timestamp
 * 4. Persistence
 *
 * Business Rules:
 * - Content type must be "movie" or "tv"
 * - Item cannot already be in watchlist
 * - Title must not be empty
 */
@OptIn(kotlin.time.ExperimentalTime::class)
class AddToWatchlistUseCase(
    private val watchlistRepository: WatchlistRepository
) : BaseUseCase<AddToWatchlistUseCase.Params, Long>() {

    data class Params(
        val tmdbId: Int,
        val title: String,
        val posterPath: String?,
        val contentType: String
    )

    companion object {
        private val VALID_CONTENT_TYPES = setOf("movie", "tv")
    }

    override suspend fun execute(params: Params): Result<Long> {
        if (params.contentType !in VALID_CONTENT_TYPES) {
            return Result.Error(NetworkError.Validation.InvalidContentType)
        }
        if (params.title.isBlank()) {
            return Result.Error(NetworkError.Validation.FieldRequired("Title"))
        }

        val exists = watchlistRepository.isInWatchlist(params.tmdbId, params.contentType)

        if (exists) {
            return Result.Error(
                NetworkError.Validation.DuplicateWatchlistEntry(params.tmdbId)
            )
        }

        val watchlistItem = WatchlistItem(
            id = 0,
            tmdbId = params.tmdbId,
            title = params.title,
            posterPath = params.posterPath,
            contentType = params.contentType,
            dateAdded = TimeProvider.now()
        )

        return watchlistRepository.addToWatchlist(watchlistItem)
    }
}
