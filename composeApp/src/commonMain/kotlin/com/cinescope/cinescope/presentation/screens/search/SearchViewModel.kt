package com.cinescope.cinescope.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.usecase.movie.SearchMoviesUseCase
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.presentation.mapper.MoviePresentationMapper
import com.cinescope.cinescope.presentation.model.MovieUi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val searchResults: List<MovieUi> = emptyList(),
    val errorMessage: String? = null
)

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .filter { it.isNotBlank() && it.length >= 2 }
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query, errorMessage = null) }

        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isLoading = false) }
        } else if (query.length >= 2) {
            _uiState.update { it.copy(isLoading = true) }
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        when (val result = searchMoviesUseCase(SearchMoviesUseCase.Params(query))) {
            is Result.Success -> {
                val moviesUi = MoviePresentationMapper.toPresentation(result.data)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        searchResults = moviesUi,
                        errorMessage = null
                    )
                }
            }
            is Result.Error -> {
                val errorMessage = when (result.error) {
                    is NetworkError.Validation.SearchQueryTooShort ->
                        "Please enter at least 2 characters to search"
                    is NetworkError.Validation.SearchQueryEmpty ->
                        "Search query cannot be empty"
                    is NetworkError.Validation.SearchQueryTooLong ->
                        "Search query is too long (max 100 characters)"
                    is NetworkError.Validation.SearchQueryInvalidCharacters ->
                        "Search contains invalid characters. Use only letters, numbers, and basic punctuation."
                    is NetworkError.NoInternet ->
                        "No internet connection. Please check your network."
                    is NetworkError.Timeout ->
                        "Search timed out. Please try again."
                    else ->
                        "Search failed: ${result.error.message}"
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
            }
            is Result.Loading -> {
                _uiState.update { it.copy(isLoading = true) }
            }
        }
    }

    fun addToLibrary(movieUi: MovieUi) {
        viewModelScope.launch {
            val domainMovie = movieRepository.getMovieByTmdbId(movieUi.tmdbId)
            if (domainMovie != null) {
                movieRepository.saveMovie(domainMovie)
            }
        }
    }
}
