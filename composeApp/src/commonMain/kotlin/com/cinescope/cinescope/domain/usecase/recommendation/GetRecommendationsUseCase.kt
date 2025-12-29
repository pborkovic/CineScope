package com.cinescope.cinescope.domain.usecase.recommendation

import com.cinescope.cinescope.domain.model.Recommendation
import com.cinescope.cinescope.domain.repository.RecommendationRepository
import com.cinescope.cinescope.domain.usecase.base.BaseUseCase
import com.cinescope.cinescope.domain.util.Result

/**
 * Use case for retrieving personalized movie recommendations.
 *
 * Wraps the recommendation repository with optional limit parameter.
 * The recommendation engine uses a hybrid algorithm combining:
 * - Content-based filtering (genre preferences from user ratings)
 * - Popularity metrics from TMDB
 *
 * While this use case is currently a simple delegation, it provides:
 * - Consistent API for ViewModels (all data through use cases)
 * - Easy testing and mocking
 * - Future extensibility (e.g., caching, filtering logic)
 */
class GetRecommendationsUseCase(
    private val recommendationRepository: RecommendationRepository
) : BaseUseCase<GetRecommendationsUseCase.Params, List<Recommendation>>() {

    data class Params(
        val limit: Int = 20
    )

    override suspend fun execute(params: Params): Result<List<Recommendation>> {
        return recommendationRepository.getRecommendations(params.limit)
    }
}
