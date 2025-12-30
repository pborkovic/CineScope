package com.cinescope.cinescope.presentation.mapper

import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.presentation.mapper.base.PresentationMapper
import com.cinescope.cinescope.presentation.model.MovieUi
import kotlin.math.roundToInt

/**
 * Mapper for converting Movie domain models to MovieUi presentation models.
 *
 * Responsibilities:
 * - Format release dates to years
 * - Format ratings with stars
 * - Format vote counts with k/M suffixes
 * - Map genre IDs to human-readable names
 * - Format runtime to hours/minutes
 * - Generate full image URLs with placeholders
 */
object MoviePresentationMapper : PresentationMapper<Movie, MovieUi> {

    private const val DEFAULT_POSTER_SIZE = "w500"
    private const val DEFAULT_BACKDROP_SIZE = "w780"
    private const val PLACEHOLDER_POSTER = "https://via.placeholder.com/500x750?text=No+Poster"
    private const val PLACEHOLDER_BACKDROP = "https://via.placeholder.com/780x439?text=No+Image"

    override fun toPresentation(domain: Movie): MovieUi {
        return MovieUi(
            id = domain.id,
            tmdbId = domain.tmdbId,
            title = domain.title,
            overview = domain.overview ?: "No description available.",
            posterUrl = domain.getPosterUrl(DEFAULT_POSTER_SIZE) ?: PLACEHOLDER_POSTER,
            backdropUrl = domain.getBackdropUrl(DEFAULT_BACKDROP_SIZE) ?: PLACEHOLDER_BACKDROP,
            releaseYear = formatReleaseYear(domain.releaseDate),
            ratingDisplay = formatRating(domain.voteAverage),
            voteCount = formatVoteCount(domain.voteCount),
            genreNames = formatGenres(domain.genreIds),
            runtime = formatRuntime(domain.runtime),
            tagline = domain.tagline ?: "",
            isWatched = false,
            userRating = null,
            isInWatchlist = false
        )
    }

    /**
     * Creates MovieUi with additional metadata from user interactions.
     *
     * Use this when you have metadata about the user's relationship with the movie.
     */
    fun toPresentationWithMetadata(
        domain: Movie,
        userRating: Double? = null,
        isInWatchlist: Boolean = false,
        isWatched: Boolean = false
    ): MovieUi {
        return toPresentation(domain).copy(
            userRating = userRating,
            isInWatchlist = isInWatchlist,
            isWatched = isWatched
        )
    }

    private fun formatReleaseYear(releaseDate: String?): String {
        return releaseDate?.take(4) ?: "Unknown"
    }

    private fun formatRating(voteAverage: Double?): String {
        return voteAverage?.let {
            val rounded = (it * 10).roundToInt() / 10.0
            val fullStars = (it / 2).toInt()
            val emptyStars = 5 - fullStars
            val stars = "★".repeat(fullStars) + "☆".repeat(emptyStars)
            "$rounded/10 $stars"
        } ?: "Not rated"
    }

    private fun formatVoteCount(voteCount: Int?): String {
        return voteCount?.let {
            when {
                it >= 1_000_000 -> "${(it / 1_000_000.0).roundToInt()}M votes"
                it >= 1_000 -> "${(it / 100.0).roundToInt() / 10.0}k votes"
                it > 0 -> "$it votes"
                else -> "No votes"
            }
        } ?: "No votes"
    }

    private fun formatGenres(genreIds: List<Int>): String {
        //TMDB standard genre ids
        val genreMap = mapOf(
            28 to "Action",
            12 to "Adventure",
            16 to "Animation",
            35 to "Comedy",
            80 to "Crime",
            99 to "Documentary",
            18 to "Drama",
            10751 to "Family",
            14 to "Fantasy",
            36 to "History",
            27 to "Horror",
            10402 to "Music",
            9648 to "Mystery",
            10749 to "Romance",
            878 to "Sci-Fi",
            10770 to "TV Movie",
            53 to "Thriller",
            10752 to "War",
            37 to "Western"
        )

        val names = genreIds.mapNotNull { genreMap[it] }
        return names.joinToString(", ").ifEmpty { "Unknown" }
    }

    private fun formatRuntime(runtime: Int?): String {
        return runtime?.let {
            val hours = it / 60
            val minutes = it % 60
            when {
                hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
                hours > 0 -> "${hours}h"
                minutes > 0 -> "${minutes}m"
                else -> "Unknown"
            }
        } ?: "Unknown"
    }
}
