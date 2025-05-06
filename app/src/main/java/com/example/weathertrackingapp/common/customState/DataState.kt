package com.example.weathertrackingapp.common.customState

import com.example.weathertrackingapp.common.weatherException.CustomException

sealed interface DataState<out T> {
    data class Success<out T>(val data: T) : DataState<T>
    data class Failure(val exception: CustomException) : DataState<Nothing>
    data class IsLoading(val isLoading: Boolean) : DataState<Nothing>
}