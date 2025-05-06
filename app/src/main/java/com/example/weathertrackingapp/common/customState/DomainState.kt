package com.example.weathertrackingapp.common.customState

import com.example.weathertrackingapp.common.customException.CustomException

sealed interface DomainState<out T> {
    data class Loading(val isLoading: Boolean) : DomainState<Nothing>
    data class Success<out T>(val data: T) : DomainState<T>
    data class Failure(val exception: CustomException) : DomainState<Nothing>
}