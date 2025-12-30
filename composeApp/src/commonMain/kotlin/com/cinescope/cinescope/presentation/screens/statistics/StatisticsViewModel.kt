package com.cinescope.cinescope.presentation.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.usecase.statistics.GetMovieStatisticsUseCase
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.presentation.mapper.StatisticsPresentationMapper
import com.cinescope.cinescope.presentation.model.StatisticsUi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI state for the statistics screen using presentation models.
 *
 * Refactored to use StatisticsUi instead of raw statistical data.
 */
data class StatisticsUiState(
    val statistics: StatisticsUi? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for the statistics screen using enterprise architecture.
 *
 * The use case orchestrates calls to multiple repositories (MovieRepository,
 * TVSeriesRepository, RatingRepository, WatchlistRepository) to gather
 * comprehensive statistics, demonstrating enterprise-grade business logic.
 */
class StatisticsViewModel(
    private val getMovieStatisticsUseCase: GetMovieStatisticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getMovieStatisticsUseCase(Unit)) {
                is Result.Success -> {
                    val statisticsUi = StatisticsPresentationMapper.toPresentation(result.data)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            statistics = statisticsUi,
                            errorMessage = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load statistics: ${result.error.message}"
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun refresh() {
        loadStatistics()
    }
}
