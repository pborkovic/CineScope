package com.cinescope.cinescope.domain.util

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val error: NetworkError) : Result<Nothing>()
    data object Loading : Result<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw Exception(error.message)
        is Loading -> throw IllegalStateException("Result is still loading")
    }

    fun errorOrNull(): NetworkError? = when (this) {
        is Error -> error
        else -> null
    }
}

inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
        is Result.Loading -> this
    }
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

inline fun <T> Result<T>.onError(action: (NetworkError) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(error)
    }
    return this
}
