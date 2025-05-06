package com.example.weathertrackingapp.common.customState

import com.example.weathertrackingapp.common.customException.CustomException

sealed interface DataState<out T> {
    data class Loading(val isLoading: Boolean) : DataState<Nothing>
    data class Success<out T>(val data: T) : DataState<T>
    data class Failure(val exception: CustomException) : DataState<Nothing>
}