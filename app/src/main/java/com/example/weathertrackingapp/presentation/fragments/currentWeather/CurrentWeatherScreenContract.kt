package com.example.weathertrackingapp.presentation.fragments.currentWeather

import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.domain.model.WeatherRequest


sealed interface UserIntent {
    data class GetCurrentWeather(val weatherRequest: WeatherRequest) : UserIntent
}

sealed interface UiEvent {
    data class ShowLoading(val isLoading: Boolean) : UiEvent
    data class Success<T>(val data: T) : UiEvent
    data class ShowError(val error: CustomException) : UiEvent
}