package com.cinescope.cinescope.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.repository.UserProfileRepository
import com.cinescope.cinescope.domain.usecase.movie.GetWatchedMoviesUseCase
import com.cinescope.cinescope.presentation.mapper.MoviePresentationMapper
import com.cinescope.cinescope.presentation.model.MovieUi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val recentMovies: List<MovieUi> = emptyList(),
    val totalMoviesWatched: Long = 0,
    val isLoading: Boolean = false,
    val userName: String? = null
)

class HomeViewModel(
    private val movieRepository: MovieRepository,
    private val userProfileRepository: UserProfileRepository,
    private val getWatchedMoviesUseCase: GetWatchedMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                userProfileRepository.getUserProfile()
                    .catch {
                    /* Ignore errors */
                    }
                    .collect { profile ->
                        _uiState.update { it.copy(userName = profile.name) }
                    }
            } catch (e: Exception) {
            }
        }
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getWatchedMoviesUseCase(Unit)
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, recentMovies = emptyList())
                    }
                }
                .collect { movies ->
                    val moviesUi = MoviePresentationMapper.toPresentation(movies)

                    val count = movieRepository.getMovieCount()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recentMovies = moviesUi.take(10),
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
