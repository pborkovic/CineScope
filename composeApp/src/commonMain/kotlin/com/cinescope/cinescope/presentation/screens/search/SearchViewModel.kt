package com.cinescope.cinescope.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.model.Movie
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.util.Result
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SearchUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val searchResults: List<Movie> = emptyList(),
    val errorMessage: String? = null
)

@OptIn(FlowPreview::class)
class SearchViewModel(
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

        when (val result = movieRepository.searchMovies(query)) {
            is Result.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        searchResults = result.data,
                        errorMessage = null
                    )
                }
            }
            is Result.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to search: ${result.error}"
                    )
                }
            }
            is Result.Loading -> {
                _uiState.update { it.copy(isLoading = true) }
            }
        }
    }

    fun addToLibrary(movie: Movie) {
        viewModelScope.launch {
            movieRepository.saveMovie(movie)
        }
    }
}
