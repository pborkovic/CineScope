package com.cinescope.cinescope.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.repository.RatingRepository
import com.cinescope.cinescope.domain.repository.WatchlistRepository
import com.cinescope.cinescope.domain.usecase.rating.AddRatingUseCase
import com.cinescope.cinescope.domain.usecase.rating.UpdateRatingUseCase
import com.cinescope.cinescope.domain.usecase.rating.DeleteRatingUseCase
import com.cinescope.cinescope.domain.usecase.watchlist.AddToWatchlistUseCase
import com.cinescope.cinescope.domain.usecase.watchlist.RemoveFromWatchlistUseCase
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.presentation.mapper.RatingPresentationMapper
import com.cinescope.cinescope.presentation.model.RatingUi
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
        val userRating: RatingUi? = null,
        val isInWatchlist: Boolean = false
    ) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}

class DetailsViewModel(
    private val movieRepository: MovieRepository,
    private val watchlistRepository: WatchlistRepository,
    private val ratingRepository: RatingRepository,
    private val addRatingUseCase: AddRatingUseCase,
    private val updateRatingUseCase: UpdateRatingUseCase,
    private val deleteRatingUseCase: DeleteRatingUseCase,
    private val addToWatchlistUseCase: AddToWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase
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
                    val localMovie = movieRepository.getMovieByTmdbId(tmdbId)

                    if (localMovie == null) {
                        when (val saveResult = movieRepository.saveMovie(movie)) {
                            is Result.Success -> currentMovieId = saveResult.data
                            else -> currentMovieId = 0
                        }
                    } else {
                        currentMovieId = localMovie.id
                    }

                    val isInWatchlist = watchlistRepository.isInWatchlist(tmdbId, "movie")

                    val existingRating = if (currentMovieId > 0) {
                        ratingRepository.getRatingsByMovieId(currentMovieId).first().firstOrNull()
                    } else null

                    existingRatingId = existingRating?.id

                    val ratingUi = existingRating?.let {
                        RatingPresentationMapper.toPresentation(it)
                    }

                    _uiState.value = DetailsUiState.Success(
                        movie = movie,
                        userRating = ratingUi,
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

    /**
     * Updates or adds a movie rating using enterprise use cases.
     *
     * Uses AddRatingUseCase for new ratings and UpdateRatingUseCase for existing ones.
     * Handles validation errors with user-friendly messages.
     */
    fun updateRating(rating: Double, review: String? = null) {
        val currentState = _uiState.value
        if (currentState is DetailsUiState.Success && currentMovieId > 0) {
            viewModelScope.launch {
                val result = if (existingRatingId != null) {
                    updateRatingUseCase(
                        UpdateRatingUseCase.Params(
                            ratingId = existingRatingId!!,
                            movieId = currentMovieId,
                            rating = rating,
                            review = review
                        )
                    )
                } else {
                    addRatingUseCase(
                        AddRatingUseCase.Params(
                            movieId = currentMovieId,
                            rating = rating,
                            review = review
                        )
                    )
                }

                when (result) {
                    is Result.Success -> {
                        if (existingRatingId == null && result.data is Long) {
                            existingRatingId = result.data
                        }

                        val freshRating = ratingRepository.getRatingsByMovieId(currentMovieId)
                            .first()
                            .firstOrNull()

                        val ratingUi = freshRating?.let {
                            RatingPresentationMapper.toPresentation(it)
                        }

                        _uiState.update {
                            DetailsUiState.Success(
                                movie = currentState.movie,
                                userRating = ratingUi,
                                isInWatchlist = currentState.isInWatchlist
                            )
                        }
                    }
                    is Result.Error -> {
                        val errorMessage = when (result.error) {
                            is NetworkError.Validation.RatingTooLow ->
                                "Rating must be at least 0.0"
                            is NetworkError.Validation.RatingTooHigh ->
                                "Rating cannot exceed 5.0"
                            is NetworkError.Validation.RatingInvalidIncrement ->
                                "Rating must be in 0.5 increments (e.g., 3.5, 4.0)"
                            is NetworkError.Validation.ReviewTooLong ->
                                "Review is too long (max 1000 characters)"
                            is NetworkError.Validation.ReviewInvalidContent ->
                                "Review contains inappropriate content"
                            else -> "Failed to save rating: ${result.error.message}"
                        }
                        _uiState.value = DetailsUiState.Error(errorMessage)
                    }
                    is Result.Loading -> {
                    }
                }
            }
        }
    }

    /**
     * Toggles movie watchlist status using enterprise use cases.
     *
     * Uses AddToWatchlistUseCase and RemoveFromWatchlistUseCase with validation.
     * Handles validation errors like duplicate entries gracefully.
     */
    fun toggleWatchlist() {
        val currentState = _uiState.value
        if (currentState is DetailsUiState.Success) {
            viewModelScope.launch {
                val movie = currentState.movie

                val result = if (currentState.isInWatchlist) {
                    removeFromWatchlistUseCase(
                        RemoveFromWatchlistUseCase.Params(
                            tmdbId = movie.tmdbId,
                            contentType = "movie"
                        )
                    )
                } else {
                    addToWatchlistUseCase(
                        AddToWatchlistUseCase.Params(
                            tmdbId = movie.tmdbId,
                            title = movie.title,
                            posterPath = movie.posterPath,
                            contentType = "movie"
                        )
                    )
                }

                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            DetailsUiState.Success(
                                movie = currentState.movie,
                                userRating = currentState.userRating,
                                isInWatchlist = !currentState.isInWatchlist
                            )
                        }
                    }
                    is Result.Error -> {
                        val errorMessage = when (result.error) {
                            is NetworkError.Validation.DuplicateWatchlistEntry ->
                                "This movie is already in your watchlist"
                            is NetworkError.Validation.InvalidContentType ->
                                "Invalid content type"
                            is NetworkError.Validation.FieldRequired ->
                                "Movie title is required"
                            else -> "Failed to update watchlist: ${result.error.message}"
                        }
                        _uiState.value = DetailsUiState.Error(errorMessage)
                    }
                    is Result.Loading -> {
                    }
                }
            }
        }
    }
}
