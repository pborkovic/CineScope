package com.cinescope.cinescope.data.repository

import com.cinescope.cinescope.domain.model.Recommendation
import com.cinescope.cinescope.domain.recommendation.RecommendationEngine
import com.cinescope.cinescope.domain.repository.RecommendationRepository
import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result

class RecommendationRepositoryImpl(
    private val recommendationEngine: RecommendationEngine
) : RecommendationRepository {

    override suspend fun getRecommendations(limit: Int): Result<List<Recommendation>> {
        return try {
            val recommendations = recommendationEngine.generateRecommendations(limit)
            Result.Success(recommendations)
        } catch (e: Exception) {
            Result.Error(NetworkError.Unknown(e.message ?: "Failed to generate recommendations"))
        }
    }

    override suspend fun getRecommendationsForMovie(tmdbId: Int): Result<List<Recommendation>> {
        // For now, return general recommendations
        return getRecommendations(10)
    }
}
