package com.cinescope.cinescope.presentation.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.model.WatchlistItem
import com.cinescope.cinescope.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class WatchlistUiState(
    val items: List<WatchlistItem> = emptyList(),
    val isLoading: Boolean = false
)

class WatchlistViewModel(
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    init {
        loadWatchlist()
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            watchlistRepository.getWatchlist()
                .catch { error ->
                    _uiState.update { it.copy(isLoading = false, items = emptyList()) }
                }
                .collect { items ->
                    _uiState.update {
                        it.copy(isLoading = false, items = items)
                    }
                }
        }
    }

    fun removeFromWatchlist(tmdbId: Int, contentType: String) {
        viewModelScope.launch {
            watchlistRepository.removeFromWatchlist(tmdbId, contentType)
        }
    }

    fun refresh() {
        loadWatchlist()
    }
}
