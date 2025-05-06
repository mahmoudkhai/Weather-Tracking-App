package com.example.weathertrackingapp.common.appState

import com.example.weathertrackingapp.common.weatherException.CustomException

sealed interface ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>
    data class Failure(val exception: CustomException) : ResultState<Nothing>
    data class IsLoading(val isLoading: Boolean) : ResultState<Nothing>
}