package com.cinescope.cinescope.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cinescope.cinescope.data.local.database.CineScopeDatabase
import com.cinescope.cinescope.domain.model.WatchlistItem
import com.cinescope.cinescope.domain.repository.WatchlistRepository
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchlistRepositoryImpl(
    private val database: CineScopeDatabase
) : WatchlistRepository {

    override suspend fun addToWatchlist(item: WatchlistItem): Result<Long> {
        return try {
            database.cineScopeDatabaseQueries.insertWatchlistItem(
                tmdbId = item.tmdbId.toLong(),
                contentType = item.contentType,
                title = item.title,
                posterPath = item.posterPath,
                dateAdded = item.dateAdded.toEpochMilliseconds()
            )
            Result.Success(item.id)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to add to watchlist"))
        }
    }

    override suspend fun removeFromWatchlist(tmdbId: Int, contentType: String): Result<Unit> {
        return try {
            database.cineScopeDatabaseQueries.deleteWatchlistItemByTmdbId(
                tmdbId = tmdbId.toLong(),
                contentType = contentType
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to remove from watchlist"))
        }
    }

    override suspend fun getWatchlist(): Flow<List<WatchlistItem>> {
        return database.cineScopeDatabaseQueries
            .getAllWatchlistItems()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { items ->
                items.map { dbItem ->
                    WatchlistItem(
                        id = dbItem.id,
                        tmdbId = dbItem.tmdbId.toInt(),
                        contentType = dbItem.contentType,
                        title = dbItem.title,
                        posterPath = dbItem.posterPath,
                        dateAdded = kotlinx.datetime.Instant.fromEpochMilliseconds(dbItem.dateAdded)
                    )
                }
            }
    }

    override suspend fun isInWatchlist(tmdbId: Int, contentType: String): Boolean {
        return try {
            database.cineScopeDatabaseQueries
                .getWatchlistItemByTmdbId(tmdbId.toLong(), contentType)
                .executeAsOneOrNull() != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getWatchlistCount(): Long {
        return try {
            database.cineScopeDatabaseQueries.getWatchlistCount().executeAsOne()
        } catch (e: Exception) {
            0L
        }
    }
}
