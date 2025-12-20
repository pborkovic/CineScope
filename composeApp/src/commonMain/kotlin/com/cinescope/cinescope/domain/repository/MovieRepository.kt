package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for movie data access and management.
 *
 * Provides an abstraction layer over local database storage and remote TMDB API access,
 * coordinating data retrieval, caching, and persistence operations. Implementations should
 * follow an offline-first strategy, returning cached data immediately while fetching fresh
 * data from remote sources.
 *
 * ## Responsibilities
 * - Search and retrieve movie data from TMDB API
 * - Cache movie data in local database
 * - Provide reactive data streams via Flow
 * - Manage CRUD operations for local movie storage
 * - Handle error cases gracefully with Result wrapper
 *
 * ## Data Flow
 * 1. UI requests data through this interface
 * 2. Implementation checks local cache first
 * 3. Returns cached data if available
 * 4. Fetches from TMDB API in background
 * 5. Updates cache with fresh data
 * 6. Emits updates via Flow
 *
 * @see com.cinescope.cinescope.data.repository.MovieRepositoryImpl
 * @see Movie
 * @see Result
 */
interface MovieRepository {
    /**
     * Searches for movies matching the given query string.
     *
     * Performs a search against the TMDB API to find movies whose titles match the query.
     * Results are cached locally for offline access and faster subsequent retrieval.
     *
     * @param query Search term to match against movie titles (case-insensitive)
     * @return [Result.Success] containing list of matching movies, or [Result.Error] on failure
     *
     * @see getMovieDetails
     */
    suspend fun searchMovies(query: String): Result<List<Movie>>

    /**
     * Retrieves detailed information for a specific movie by its TMDB ID.
     *
     * Fetches comprehensive movie data from the TMDB API including cast, crew, budget,
     * revenue, and other metadata. The result is cached locally for offline access.
     *
     * @param tmdbId The Movie Database unique identifier for the movie
     * @return [Result.Success] containing the movie entity, or [Result.Error] if not found or network error
     *
     * @see searchMovies
     * @see saveMovie
     */
    suspend fun getMovieDetails(tmdbId: Int): Result<Movie>

    /**
     * Retrieves all movies the user has watched as a reactive stream.
     *
     * Returns a Flow that automatically emits updated lists whenever the watched movies
     * collection changes (e.g., when new movies are added or removed). Useful for
     * reactive UI updates in screens displaying the user's library.
     *
     * @return Flow emitting lists of watched movies, ordered by date added (most recent first)
     *
     * @see saveMovie
     * @see deleteMovie
     */
    suspend fun getWatchedMovies(): Flow<List<Movie>>

    /**
     * Retrieves a movie from local storage by its TMDB ID.
     *
     * Performs a local database lookup without making network requests. Returns null
     * if the movie hasn't been cached locally yet.
     *
     * @param tmdbId The Movie Database unique identifier
     * @return Movie entity if found in local cache, null otherwise
     *
     * @see getMovieDetails
     */
    suspend fun getMovieByTmdbId(tmdbId: Int): Movie?

    /**
     * Saves a movie to the local database.
     *
     * Persists movie data to local storage for offline access and library management.
     * If a movie with the same TMDB ID already exists, it will be updated with the new data.
     *
     * @param movie The movie entity to save
     * @return [Result.Success] containing the auto-generated database ID, or [Result.Error] on failure
     *
     * @see deleteMovie
     * @see getWatchedMovies
     */
    suspend fun saveMovie(movie: Movie): Result<Long>

    /**
     * Deletes a movie from the local database.
     *
     * Removes the movie and all associated data (ratings, watchlist entries) from local storage.
     * This operation is permanent and cannot be undone.
     *
     * @param id Local database identifier of the movie to delete
     * @return [Result.Success] on successful deletion, or [Result.Error] on failure
     *
     * @see saveMovie
     */
    suspend fun deleteMovie(id: Long): Result<Unit>

    /**
     * Counts the total number of movies in the user's library.
     *
     * Returns the count of unique movies stored in the local database,
     * useful for statistics and insights features.
     *
     * @return Total number of movies in local storage
     *
     * @see getWatchedMovies
     */
    suspend fun getMovieCount(): Long
}
