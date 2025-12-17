package com.cinescope.cinescope.domain.repository

import com.cinescope.cinescope.domain.model.Recommendation
import com.cinescope.cinescope.domain.util.Result

interface RecommendationRepository {
    suspend fun getRecommendations(limit: Int = 20): Result<List<Recommendation>>

    suspend fun getRecommendationsForMovie(tmdbId: Int): Result<List<Recommendation>>
}
