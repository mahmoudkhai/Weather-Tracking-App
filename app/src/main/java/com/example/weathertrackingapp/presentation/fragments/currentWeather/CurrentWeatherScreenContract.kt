package com.example.weathertrackingapp.presentation.fragments.currentWeather

import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest


sealed interface CurrentWeatherScreenContract {
    sealed interface Intent {
        data class GetCurrentWeather(val weatherRequest: WeatherRequest) : Intent
    }
}