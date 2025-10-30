package com.example.smartschedule.data.repository

/**
 * A sealed class to represent the state of a data request.
 */
sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}
