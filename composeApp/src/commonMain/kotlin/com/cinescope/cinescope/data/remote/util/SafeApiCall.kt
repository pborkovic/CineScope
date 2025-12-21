package com.cinescope.cinescope.data.remote.util

import com.cinescope.cinescope.domain.util.NetworkError
import com.cinescope.cinescope.domain.util.Result
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.TimeoutCancellationException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.Success(apiCall())
    } catch (e: Exception) {
        when (e) {
            is ClientRequestException -> {
                Result.Error(NetworkError.fromHttpCode(e.response.status.value))
            }
            is ServerResponseException -> {
                Result.Error(NetworkError.ServerError)
            }
            is HttpRequestTimeoutException, is TimeoutCancellationException -> {
                Result.Error(NetworkError.Timeout)
            }
            is IOException -> {
                Result.Error(NetworkError.NoInternet)
            }
            else -> {
                Result.Error(NetworkError.Unknown(e.message ?: "Unknown error"))
            }
        }
    }
}
