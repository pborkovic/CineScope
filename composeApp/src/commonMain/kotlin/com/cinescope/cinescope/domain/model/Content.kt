package com.cinescope.cinescope.domain.model

import kotlinx.datetime.Instant

/**
 * Sealed interface representing any type of watchable content in CineScope.
 *
 * This abstraction allows uniform handling of different content types (movies and TV series)
 * throughout the application. It provides a common set of properties and operations that
 * apply to all content types, enabling polymorphic behavior in the UI and business logic.
 *
 * @property id Local database identifier
 * @property tmdbId The Movie Database unique identifier
 * @property title Display title of the content
 * @property overview Brief description or plot summary
 * @property posterPath Relative path to the poster image
 * @property backdropPath Relative path to the backdrop image
 * @property voteAverage Average user rating from TMDB (0.0 to 10.0)
 * @property popularity TMDB popularity metric
 * @property genreIds List of TMDB genre identifiers
 * @property dateAdded Timestamp when added to local database
 *
 * @see MovieContent
 * @see TVSeriesContent
 */
sealed interface Content {
    val id: Long
    val tmdbId: Int
    val title: String
    val overview: String?
    val posterPath: String?
    val backdropPath: String?
    val voteAverage: Double?
    val popularity: Double?
    val genreIds: List<Int>
    val dateAdded: Instant

    /**
     * Constructs a full TMDB image URL for the content poster.
     *
     * @param size The desired image size
     * @return Complete TMDB image URL, or null if no poster path is available
     */
    fun getPosterUrl(size: String = "w500"): String?

    /**
     * Constructs a full TMDB image URL for the content backdrop.
     *
     * @param size The desired image size
     * @return Complete TMDB image URL, or null if no backdrop path is available
     */
    fun getBackdropUrl(size: String = "w780"): String?
}

/**
 * Wrapper class that adapts a [Movie] to the [Content] interface.
 *
 * This implementation delegates all Content interface methods to the underlying
 * Movie instance, allowing movies to be treated as generic content.
 *
 * @property movie The underlying movie instance
 * @constructor Creates a MovieContent wrapper around a movie
 */
data class MovieContent(val movie: Movie) : Content {
    override val id: Long get() = movie.id
    override val tmdbId: Int get() = movie.tmdbId
    override val title: String get() = movie.title
    override val overview: String? get() = movie.overview
    override val posterPath: String? get() = movie.posterPath
    override val backdropPath: String? get() = movie.backdropPath
    override val voteAverage: Double? get() = movie.voteAverage
    override val popularity: Double? get() = movie.popularity
    override val genreIds: List<Int> get() = movie.genreIds
    override val dateAdded: Instant get() = movie.dateAdded

    override fun getPosterUrl(size: String): String? = movie.getPosterUrl(size)
    override fun getBackdropUrl(size: String): String? = movie.getBackdropUrl(size)
}

/**
 * Wrapper class that adapts a [TVSeries] to the [Content] interface.
 *
 * This implementation delegates all Content interface methods to the underlying
 * TVSeries instance, allowing TV series to be treated as generic content.
 *
 * @property series The underlying TV series instance
 * @constructor Creates a TVSeriesContent wrapper around a TV series
 */
data class TVSeriesContent(val series: TVSeries) : Content {
    override val id: Long get() = series.id
    override val tmdbId: Int get() = series.tmdbId
    override val title: String get() = series.name
    override val overview: String? get() = series.overview
    override val posterPath: String? get() = series.posterPath
    override val backdropPath: String? get() = series.backdropPath
    override val voteAverage: Double? get() = series.voteAverage
    override val popularity: Double? get() = series.popularity
    override val genreIds: List<Int> get() = series.genreIds
    override val dateAdded: Instant get() = series.dateAdded

    override fun getPosterUrl(size: String): String? = series.getPosterUrl(size)
    override fun getBackdropUrl(size: String): String? = series.getBackdropUrl(size)
}

/**
 * Enumeration of content types supported by the application.
 *
 * Used for filtering and categorizing content in various features like
 * search, recommendations, and library management.
 */
enum class ContentType {
    MOVIE,
    TV_SERIES,
    BOTH
}
