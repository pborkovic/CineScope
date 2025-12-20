package com.cinescope.cinescope.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDto(
    @SerialName("id") val id: Int,
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("air_date") val airDate: String? = null,
    @SerialName("episode_count") val episodeCount: Int
)

@Serializable
data class SeasonDetailsDto(
    @SerialName("id") val id: Int,
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("air_date") val airDate: String? = null,
    @SerialName("episodes") val episodes: List<EpisodeDto> = emptyList()
)
