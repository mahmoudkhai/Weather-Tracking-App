package com.example.weathertrackingapp.presentation.presentationUtil

import com.example.weathertrackingapp.common.customException.CustomException

sealed interface UiEvent {
    data class ShowLoading(val isLoading: Boolean) : UiEvent
    data class SuccessWithFreshData<T>(val data: T) : UiEvent
    data class SuccessWithCachedData<T>(val data: T) : UiEvent
    data class ShowError(val error: List<CustomException>) : UiEvent
}