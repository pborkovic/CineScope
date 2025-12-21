package com.cinescope.cinescope.domain.model

import kotlinx.datetime.Instant

@OptIn(kotlin.time.ExperimentalTime::class)
/**
 * Represents a TV series entity in the CineScope application.
 *
 * This data class contains comprehensive TV series information from TMDB (The Movie Database)
 * including metadata, ratings, air dates, season information, and production status. It serves
 * as the core domain model for TV series-related operations throughout the application.
 *
 * @property id Local database identifier (auto-generated, 0 for new entries)
 * @property tmdbId The Movie Database unique identifier for this TV series
 * @property name The primary display name of the TV series
 * @property originalName The original name in the series' native language
 * @property overview A brief plot summary or description of the series
 * @property posterPath Relative path to the series poster image on TMDB servers
 * @property backdropPath Relative path to the backdrop/header image on TMDB servers
 * @property firstAirDate The first episode air date in ISO 8601 format (YYYY-MM-DD)
 * @property lastAirDate The most recent episode air date in ISO 8601 format
 * @property voteAverage Average user rating from TMDB (0.0 to 10.0 scale)
 * @property voteCount Total number of user votes on TMDB
 * @property popularity TMDB popularity metric (higher values indicate trending content)
 * @property originalLanguage ISO 639-1 language code of the series' original language
 * @property adult Whether the series is classified as adult content
 * @property genreIds List of TMDB genre identifiers associated with this series
 * @property numberOfSeasons Total number of seasons in the series
 * @property numberOfEpisodes Total number of episodes across all seasons
 * @property status Current production status (e.g., "Returning Series", "Ended", "Canceled")
 * @property tagline The series' marketing tagline
 * @property type Series type (e.g., "Scripted", "Reality", "Documentary", "News")
 * @property inProduction Whether the series is currently in active production
 * @property dateAdded Timestamp when this series was added to the local database
 *
 * @see [TMDB TV API Documentation](https://developers.themoviedb.org/3/tv)
 */
data class TVSeries(
    val id: Long = 0,
    val tmdbId: Int,
    val name: String,
    val originalName: String? = null,
    val overview: String? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val firstAirDate: String? = null,
    val lastAirDate: String? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val popularity: Double? = null,
    val originalLanguage: String? = null,
    val adult: Boolean = false,
    val genreIds: List<Int> = emptyList(),
    val numberOfSeasons: Int? = null,
    val numberOfEpisodes: Int? = null,
    val status: String? = null,
    val tagline: String? = null,
    val type: String? = null,
    val inProduction: Boolean = false,
    val dateAdded: Instant = Instant.DISTANT_PAST
) {
    /**
     * Constructs a full TMDB image URL for the TV series poster.
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
     * Constructs a full TMDB image URL for the TV series backdrop.
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
