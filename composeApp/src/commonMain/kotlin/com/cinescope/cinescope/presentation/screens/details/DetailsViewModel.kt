package com.cinescope.cinescope.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Success(val movie: Movie, val userRating: Double? = null) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}

class DetailsViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun loadMovieDetails(tmdbId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading

            when (val result = movieRepository.getMovieDetails(tmdbId)) {
                is Result.Success -> {
                    _uiState.value = DetailsUiState.Success(movie = result.data)
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
        if (currentState is DetailsUiState.Success) {
            _uiState.update {
                DetailsUiState.Success(
                    movie = currentState.movie,
                    userRating = rating
                )
            }
        }
    }
}
