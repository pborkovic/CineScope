package com.cinescope.cinescope.presentation.screens.recommendations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.usecase.recommendation.GetRecommendationsUseCase
import com.cinescope.cinescope.domain.util.Result
import com.cinescope.cinescope.presentation.mapper.RecommendationPresentationMapper
import com.cinescope.cinescope.presentation.model.RecommendationUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for the recommendations screen using presentation models.
 *
 * Refactored to use RecommendationUi instead of domain Recommendation models.
 */
data class RecommendationsUiState(
    val recommendations: List<RecommendationUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel for the recommendations screen using enterprise architecture.
 *
 * Refactored to use:
 * - GetRecommendationsUseCase for business logic
 * - RecommendationUi presentation models for UI-optimized data
 * - RecommendationPresentationMapper for domain → UI transformation
 */
class RecommendationsViewModel(
    private val getRecommendationsUseCase: GetRecommendationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecommendationsUiState())
    val uiState: StateFlow<RecommendationsUiState> = _uiState.asStateFlow()

    init {
        loadRecommendations()
    }

    fun loadRecommendations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getRecommendationsUseCase(GetRecommendationsUseCase.Params(limit = 20))) {
                is Result.Success -> {
                    val recommendationsUi = RecommendationPresentationMapper.toPresentation(result.data)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recommendations = recommendationsUi,
                            errorMessage = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Failed to load recommendations"
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
        loadRecommendations()
    }
}
