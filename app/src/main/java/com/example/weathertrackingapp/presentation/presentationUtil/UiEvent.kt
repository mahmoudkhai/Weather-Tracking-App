package com.example.weathertrackingapp.presentation.presentationUtil

import com.example.weathertrackingapp.common.customException.CustomException

sealed interface UiEvent {
    data class ShowLoading(val isLoading: Boolean) : UiEvent
    data class Success<T>(val data: T) : UiEvent
    data class ShowError(val error: CustomException) : UiEvent
}