package com.example.weathertrackingapp.domain.customState

import com.example.weathertrackingapp.common.customException.CustomException

sealed interface DomainState<out T> {
    data class Loading(val isLoading: Boolean) : DomainState<Nothing>
    data class SuccessWithFreshData<out T>(val data: T) : DomainState<T>
    data class FailureWithCachedData<out T>(
        val exception: List<CustomException>, // exception of remote api and maybe an exception be thrown from localDS
        val cachedData: T? = null,
    ) :
        DomainState<T>
}