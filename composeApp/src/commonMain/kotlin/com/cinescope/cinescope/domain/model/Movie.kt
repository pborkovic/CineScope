package com.cinescope.cinescope.domain.model

import kotlinx.datetime.Instant

@OptIn(kotlin.time.ExperimentalTime::class)
/**
 * Represents a movie entity in the CineScope application.
 *
 * This data class contains comprehensive movie information from TMDB (The Movie Database)
 * including metadata, ratings, release information, and financial data. It serves as the
 * core domain model for movie-related operations throughout the application.
 *
 * @property id Local database identifier (auto-generated, 0 for new entries)
 * @property tmdbId The Movie Database unique identifier for this movie
 * @property title The primary display title of the movie
 * @property originalTitle The original title in the movie's native language
 * @property overview A brief plot summary or description of the movie
 * @property posterPath Relative path to the movie poster image on TMDB servers
 * @property backdropPath Relative path to the backdrop/header image on TMDB servers
 * @property releaseDate The theatrical release date in ISO 8601 format (YYYY-MM-DD)
 * @property voteAverage Average user rating from TMDB (0.0 to 10.0 scale)
 * @property voteCount Total number of user votes on TMDB
 * @property popularity TMDB popularity metric (higher values indicate trending content)
 * @property originalLanguage ISO 639-1 language code of the movie's original language
 * @property adult Whether the movie is classified as adult content
 * @property video Whether this is a video release (as opposed to theatrical)
 * @property genreIds List of TMDB genre identifiers associated with this movie
 * @property runtime Total runtime of the movie in minutes
 * @property budget Production budget in US dollars
 * @property revenue Box office revenue in US dollars
 * @property status Release status (e.g., "Released", "Post Production", "Rumored")
 * @property tagline The movie's marketing tagline
 * @property dateAdded Timestamp when this movie was added to the local database
 *
 * @see [TMDB API Documentation](https://developers.themoviedb.org/3/movies)
 */
data class Movie(
    val id: Long = 0,
    val tmdbId: Int,
    val title: String,
    val originalTitle: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val popularity: Double? = null,
    val originalLanguage: String? = null,
    val adult: Boolean = false,
    val video: Boolean = false,
    val genreIds: List<Int> = emptyList(),
    val runtime: Int? = null,
    val budget: Long? = null,
    val revenue: Long? = null,
    val status: String? = null,
    val tagline: String? = null,
    val dateAdded: Instant = Instant.DISTANT_PAST
) {
    /**
     * Constructs a full TMDB image URL for the movie poster.
     *
     * @param size The desired image size (e.g., "w92", "w154", "w185", "w342", "w500", "w780", "original")
     * @return Complete TMDB image URL, or null if no poster path is available
     *
     * @see [TMDB Image Configuration](https://developers.themoviedb.org/3/configuration)
     */
    fun getPosterUrl(size: String = "w500"): String? {
        return posterPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }

    /**
     * Constructs a full TMDB image URL for the movie backdrop.
     *
     * @param size The desired image size (e.g., "w300", "w780", "w1280", "original")
     * @return Complete TMDB image URL, or null if no backdrop path is available
     *
     * @see [TMDB Image Configuration](https://developers.themoviedb.org/3/configuration)
     */
    fun getBackdropUrl(size: String = "w780"): String? {
        return backdropPath?.let { "https://image.tmdb.org/t/p/$size$it" }
    }
}
