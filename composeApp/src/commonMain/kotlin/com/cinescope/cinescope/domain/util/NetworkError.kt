package com.cinescope.cinescope.domain.util

sealed class NetworkError(val message: String) {
    data object NoInternet : NetworkError("No internet connection available")
    data object Timeout : NetworkError("Request timed out")
    data object ServerError : NetworkError("Server error occurred")
    data class NotFound(val resource: String) : NetworkError("$resource not found")
    data class Unauthorized(val reason: String = "Unauthorized access") : NetworkError(reason)
    data class BadRequest(val reason: String = "Bad request") : NetworkError(reason)
    data class Unknown(val error: String = "Unknown error occurred") : NetworkError(error)

    companion object {
        fun fromHttpCode(code: Int, message: String? = null): NetworkError {
            return when (code) {
                400 -> BadRequest(message ?: "Bad request")
                401 -> Unauthorized(message ?: "Unauthorized")
                404 -> NotFound(message ?: "Resource")
                in 500..599 -> ServerError
                else -> Unknown(message ?: "HTTP $code")
            }
        }
    }
}
