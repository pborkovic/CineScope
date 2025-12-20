package com.cinescope.cinescope.data.remote.api

import com.cinescope.cinescope.data.remote.dto.*
import com.cinescope.cinescope.data.remote.util.safeApiCall
import com.cinescope.cinescope.domain.util.Result
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TmdbApiClient(
    private val httpClient: HttpClient,
    private val apiKey: String
) {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p"
    }

    suspend fun searchMovies(query: String, page: Int = 1): Result<MovieSearchResponse> {
        return safeApiCall {
            httpClient.get("$BASE_URL/search/movie") {
                parameter("api_key", apiKey)
                parameter("query", query)
                parameter("page", page)
            }.body()
        }
    }

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsDto> {
        return safeApiCall {
            httpClient.get("$BASE_URL/movie/$movieId") {
                parameter("api_key", apiKey)
            }.body()
        }
    }

    suspend fun getPopularMovies(page: Int = 1): Result<MovieSearchResponse> {
        return safeApiCall {
            httpClient.get("$BASE_URL/movie/popular") {
                parameter("api_key", apiKey)
                parameter("page", page)
            }.body()
        }
    }

    suspend fun getTrendingMovies(timeWindow: String = "week"): Result<MovieSearchResponse> {
        return safeApiCall {
            httpClient.get("$BASE_URL/trending/movie/$timeWindow") {
                parameter("api_key", apiKey)
            }.body()
        }
    }

    suspend fun getSimilarMovies(movieId: Int, page: Int = 1): Result<MovieSearchResponse> {
        return safeApiCall {
            httpClient.get("$BASE_URL/movie/$movieId/similar") {
                parameter("api_key", apiKey)
                parameter("page", page)
            }.body()
        }
    }

    suspend fun searchTVSeries(query: String, page: Int = 1): Result<TVSearchResponse> {
        return safeApiCall {
            httpClient.get("$BASE_URL/search/tv") {
                parameter("api_key", apiKey)
                parameter("query", query)
                parameter("page", page)
            }.body()
        }
    }

    suspend fun getTVSeriesDetails(seriesId: Int): Result<TVSeriesDetailsDto> {
        return safeApiCall {
            httpClient.get("$BASE_URL/tv/$seriesId") {
                parameter("api_key", apiKey)
            }.body()
        }
    }

    suspend fun getPopularTVSeries(page: Int = 1): Result<TVSearchResponse> {
        return safeApiCall {
            httpClient.get("$BASE_URL/tv/popular") {
                parameter("api_key", apiKey)
                parameter("page", page)
            }.body()
        }
    }

    suspend fun getTrendingTVSeries(timeWindow: String = "week"): Result<TVSearchResponse> {
        return safeApiCall {
            httpClient.get("$BASE_URL/trending/tv/$timeWindow") {
                parameter("api_key", apiKey)
            }.body()
        }
    }

    suspend fun getSimilarTVSeries(seriesId: Int, page: Int = 1): Result<TVSearchResponse> {
        return safeApiCall {
            httpClient.get("$BASE_URL/tv/$seriesId/similar") {
                parameter("api_key", apiKey)
                parameter("page", page)
            }.body()
        }
    }

    suspend fun getSeasonDetails(seriesId: Int, seasonNumber: Int): Result<SeasonDetailsDto> {
        return safeApiCall {
            httpClient.get("$BASE_URL/tv/$seriesId/season/$seasonNumber") {
                parameter("api_key", apiKey)
            }.body()
        }
    }

    suspend fun getMovieGenres(): Result<GenreListDto> {
        return safeApiCall {
            httpClient.get("$BASE_URL/genre/movie/list") {
                parameter("api_key", apiKey)
            }.body()
        }
    }

    suspend fun getTVGenres(): Result<GenreListDto> {
        return safeApiCall {
            httpClient.get("$BASE_URL/genre/tv/list") {
                parameter("api_key", apiKey)
            }.body()
        }
    }
}
