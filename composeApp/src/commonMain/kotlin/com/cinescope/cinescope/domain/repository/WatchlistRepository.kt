package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.WatchlistItem
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for watchlist data access and management.
 *
 * Manages the user's watchlist of movies and TV series they want to watch later.
 * Provides simple CRUD operations for adding, removing, and querying watchlist items.
 * All watchlist data is stored locally in the SQLDelight database.
 *
 * ## Responsibilities
 * - Add movies/series to the watchlist
 * - Remove items from the watchlist
 * - Query watchlist status for UI updates
 * - Provide reactive watchlist streams
 * - Maintain watchlist metadata (date added)
 *
 * ## Watchlist Features
 * - Supports both movies and TV series
 * - Prevents duplicate entries (same TMDB ID + content type)
 * - Tracks when items were added
 * - Lightweight storage (minimal data per item)
 *
 * @see com.cinescope.cinescope.data.repository.WatchlistRepositoryImpl
 * @see WatchlistItem
 * @see Result
 */
interface WatchlistRepository {
    /**
     * Adds a movie or TV series to the watchlist.
     *
     * Creates a new watchlist entry with the current timestamp. If the item already
     * exists in the watchlist (same TMDB ID and content type), the operation will fail
     * with a database constraint error.
     *
     * @param item The watchlist item to add (must include valid TMDB ID and content type)
     * @return [Result.Success] containing the auto-generated database ID, or [Result.Error] on failure
     *
     * @see removeFromWatchlist
     * @see isInWatchlist
     */
    suspend fun addToWatchlist(item: WatchlistItem): Result<Long>

    /**
     * Removes a movie or TV series from the watchlist.
     *
     * Deletes the watchlist entry matching the specified TMDB ID and content type.
     * This operation is idempotent - removing a non-existent item will succeed.
     *
     * @param tmdbId The Movie Database unique identifier
     * @param contentType Content type: "movie" for movies, "tv" for TV series
     * @return [Result.Success] on successful removal, or [Result.Error] on database failure
     *
     * @see addToWatchlist
     * @see getWatchlist
     */
    suspend fun removeFromWatchlist(tmdbId: Int, contentType: String): Result<Unit>

    /**
     * Retrieves the complete watchlist as a reactive stream.
     *
     * Returns a Flow that automatically emits updated watchlist collections whenever
     * items are added or removed. Items are ordered by date added (most recent first).
     *
     * @return Flow emitting lists of watchlist items, or empty list if watchlist is empty
     *
     * @see addToWatchlist
     * @see removeFromWatchlist
     */
    suspend fun getWatchlist(): Flow<List<WatchlistItem>>

    /**
     * Checks if a specific movie or TV series is in the watchlist.
     *
     * Performs a fast lookup to determine if content is bookmarked for later viewing.
     * Useful for UI state management (showing "Add to Watchlist" vs "Remove from Watchlist").
     *
     * @param tmdbId The Movie Database unique identifier
     * @param contentType Content type: "movie" for movies, "tv" for TV series
     * @return true if the item is in the watchlist, false otherwise
     *
     * @see addToWatchlist
     * @see removeFromWatchlist
     */
    suspend fun isInWatchlist(tmdbId: Int, contentType: String): Boolean

    /**
     * Counts the total number of items in the watchlist.
     *
     * Returns the count of unique watchlist entries, useful for statistics
     * displays and empty state checks.
     *
     * @return Total number of items in the watchlist
     *
     * @see getWatchlist
     */
    suspend fun getWatchlistCount(): Long
}
