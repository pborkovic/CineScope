package com.cinescope.cinescope.presentation.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinescope.cinescope.domain.repository.MovieRepository
import com.cinescope.cinescope.domain.repository.RatingRepository
import com.cinescope.cinescope.domain.repository.TVSeriesRepository
import com.cinescope.cinescope.domain.repository.WatchlistRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class StatisticsUiState(
    val totalMovies: Long = 0,
    val totalTVSeries: Long = 0,
    val totalRatings: Long = 0,
    val averageRating: Double = 0.0,
    val watchlistCount: Long = 0,
    val topGenres: List<Pair<String, Int>> = emptyList(),
    val ratingDistribution: Map<Int, Int> = emptyMap(),
    val isLoading: Boolean = false
)

class StatisticsViewModel(
    private val movieRepository: MovieRepository,
    private val tvSeriesRepository: TVSeriesRepository,
    private val ratingRepository: RatingRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val movieCount = movieRepository.getMovieCount()
            val tvSeriesCount = tvSeriesRepository.getTVSeriesCount()
            val ratingCount = ratingRepository.getRatingCount()
            val avgRating = ratingRepository.getAverageRating() ?: 0.0
            val watchlist = watchlistRepository.getWatchlistCount()
            val ratings = ratingRepository.getAllRatings().first()
            val distribution = ratings
                .groupBy { it.rating.toInt() }
                .mapValues { it.value.size }
            val topGenres = listOf(
                "Action" to 15,
                "Drama" to 12,
                "Comedy" to 10,
                "Sci-Fi" to 8,
                "Thriller" to 6
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    totalMovies = movieCount,
                    totalTVSeries = tvSeriesCount,
                    totalRatings = ratingCount,
                    averageRating = avgRating,
                    watchlistCount = watchlist,
                    topGenres = topGenres,
                    ratingDistribution = distribution
                )
            }
        }
    }

    fun refresh() {
        loadStatistics()
    }
}
