package com.example.weathertrackingapp.domain.customState

import com.example.weathertrackingapp.common.customException.CustomException

/**
 * Sealed interface representing the state of a domain operation.
 * It encapsulates different possible states (loading, success, failure) for a given data type.
 *
 * @param T The type of data the state holds (or `Nothing` for loading state).
 */
sealed interface DomainState<out T> {

    data class Loading(val isLoading: Boolean) : DomainState<Nothing>
    data class SuccessWithFreshData<out T>(val data: T) : DomainState<T>

    /**
     * Represents a failure in the operation, with cached data available (if any).
     *
     * @property exception A list of exceptions that occurred.
     * @property cachedData The cached data, if available, even when the operation failed.
     */
    data class FailureWithCachedData<out T>(
        val exception: List<CustomException>, // exception of remote api and maybe an exception be thrown from localDS
        val cachedData: T? = null,
    ) :
        DomainState<T>
}