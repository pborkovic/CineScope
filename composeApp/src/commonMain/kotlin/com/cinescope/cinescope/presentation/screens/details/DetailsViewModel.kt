package com.cinescope.cinescope.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.model.Rating
import com.cinescope.cinescope.domain.model.WatchlistItem
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.repository.RatingRepository
import com.cinescope.cinescope.domain.repository.WatchlistRepository
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.domain.util.TimeProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Success(
        val movie: Movie,
        val userRating: Double? = null,
        val isInWatchlist: Boolean = false
    ) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}

class DetailsViewModel(
    private val movieRepository: MovieRepository,
    private val watchlistRepository: WatchlistRepository,
    private val ratingRepository: RatingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    private var currentMovieId: Long = 0
    private var existingRatingId: Long? = null

    fun loadMovieDetails(tmdbId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading

            when (val result = movieRepository.getMovieDetails(tmdbId)) {
                is Result.Success -> {
                    val movie = result.data

                    // Save movie to local database if not already saved
                    val localMovie = movieRepository.getMovieByTmdbId(tmdbId)
                    if (localMovie == null) {
                        when (val saveResult = movieRepository.saveMovie(movie)) {
                            is Result.Success -> currentMovieId = saveResult.data
                            else -> currentMovieId = 0
                        }
                    } else {
                        currentMovieId = localMovie.id
                    }

                    // Check if movie is in watchlist
                    val isInWatchlist = watchlistRepository.isInWatchlist(tmdbId, "movie")

                    // Load existing rating if any
                    val existingRating = if (currentMovieId > 0) {
                        ratingRepository.getRatingsByMovieId(currentMovieId).first().firstOrNull()
                    } else null

                    existingRatingId = existingRating?.id

                    _uiState.value = DetailsUiState.Success(
                        movie = movie,
                        userRating = existingRating?.rating,
                        isInWatchlist = isInWatchlist
                    )
                }
                is Result.Error -> {
                    _uiState.value = DetailsUiState.Error(message = "Failed to load movie details")
                }
                is Result.Loading -> {
                    _uiState.value = DetailsUiState.Loading
                }
            }
        }
    }

    fun updateRating(rating: Double) {
        val currentState = _uiState.value
        if (currentState is DetailsUiState.Success && currentMovieId > 0) {
            viewModelScope.launch {
                // Save rating to database
                val ratingToSave = Rating(
                    id = existingRatingId ?: 0,
                    movieId = currentMovieId,
                    rating = rating,
                    review = null,
                    watchedDate = TimeProvider.now(),
                    createdAt = TimeProvider.now(),
                    updatedAt = TimeProvider.now()
                )

                if (existingRatingId != null) {
                    // Update existing rating
                    ratingRepository.updateRating(ratingToSave)
                } else {
                    // Add new rating
                    when (val result = ratingRepository.addRating(ratingToSave)) {
                        is Result.Success -> existingRatingId = result.data
                        else -> {}
                    }
                }

                // Update UI state
                _uiState.update {
                    DetailsUiState.Success(
                        movie = currentState.movie,
                        userRating = rating,
                        isInWatchlist = currentState.isInWatchlist
                    )
                }
            }
        }
    }

    fun toggleWatchlist() {
        val currentState = _uiState.value
        if (currentState is DetailsUiState.Success) {
            viewModelScope.launch {
                val movie = currentState.movie

                if (currentState.isInWatchlist) {
                    // Remove from watchlist
                    watchlistRepository.removeFromWatchlist(movie.tmdbId, "movie")
                    _uiState.update {
                        DetailsUiState.Success(
                            movie = currentState.movie,
                            userRating = currentState.userRating,
                            isInWatchlist = false
                        )
                    }
                } else {
                    // Add to watchlist
                    val watchlistItem = WatchlistItem(
                        id = 0, // Auto-generated by database
                        tmdbId = movie.tmdbId,
                        contentType = "movie",
                        title = movie.title,
                        posterPath = movie.posterPath,
                        dateAdded = TimeProvider.now()
                    )
                    watchlistRepository.addToWatchlist(watchlistItem)
                    _uiState.update {
                        DetailsUiState.Success(
                            movie = currentState.movie,
                            userRating = currentState.userRating,
                            isInWatchlist = true
                        )
                    }
                }
            }
        }
    }
}
