package com.example.weathertrackingapp.common.appState

import com.example.weathertrackingapp.common.weatherException.CustomException

sealed interface AppState<out T> {
    data class Success<out T>(val data: T) : AppState<T>
    data class Failure(val exception: CustomException) : AppState<Nothing>
    data class IsLoading(val isLoading: Boolean) : AppState<Nothing>
}