package com.cinescope.cinescope.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.repository.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val recentMovies: List<Movie> = emptyList(),
    val totalMoviesWatched: Long = 0,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            movieRepository.getWatchedMovies()
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, recentMovies = emptyList())
                    }
                }
                .collect { movies ->
                    val count = movieRepository.getMovieCount()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recentMovies = movies.take(10),
                            totalMoviesWatched = count
                        )
                    }
                }
        }
    }

    fun refresh() {
        loadHomeData()
    }
}
