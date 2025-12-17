package com.cinescope.cinescope.domain.model

data class Recommendation(
    val movie: Movie,
    val matchScore: Double,
    val reason: String
) {
    init {
        require(matchScore in 0.0..1.0) { "Match score must be between 0.0 and 1.0" }
    }
}
