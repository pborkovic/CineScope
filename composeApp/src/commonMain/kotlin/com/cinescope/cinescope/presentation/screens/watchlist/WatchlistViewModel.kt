package com.cinescope.cinescope.presentation.screens.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.usecase.watchlist.GetWatchlistUseCase
import com.cinescope.cinescope.domain.usecase.watchlist.RemoveFromWatchlistUseCase
import com.cinescope.cinescope.presentation.mapper.WatchlistPresentationMapper
import com.cinescope.cinescope.presentation.model.WatchlistItemUi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI state for the watchlist screen using presentation models.
 *
 * Refactored to use WatchlistItemUi instead of domain WatchlistItem models.
 */
data class WatchlistUiState(
    val items: List<WatchlistItemUi> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * ViewModel for the watchlist screen using enterprise architecture.
 *
 * Refactored to use:
 * - GetWatchlistUseCase for reactive watchlist data
 * - RemoveFromWatchlistUseCase for removing items with validation
 * - WatchlistItemUi presentation models for UI-optimized data
 */
class WatchlistViewModel(
    private val getWatchlistUseCase: GetWatchlistUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    init {
        loadWatchlist()
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getWatchlistUseCase(Unit)
                .catch { error ->
                    _uiState.update { it.copy(isLoading = false, items = emptyList()) }
                }
                .collect { items ->
                    // Map domain items to presentation models
                    val itemsUi = WatchlistPresentationMapper.toPresentation(items)

                    _uiState.update {
                        it.copy(isLoading = false, items = itemsUi)
                    }
                }
        }
    }

    fun removeFromWatchlist(tmdbId: Int, contentType: String) {
        viewModelScope.launch {
            removeFromWatchlistUseCase(
                RemoveFromWatchlistUseCase.Params(
                    tmdbId = tmdbId,
                    contentType = contentType
                )
            )
            // Flow will automatically update the watchlist
        }
    }

    fun refresh() {
        loadWatchlist()
    }
}
