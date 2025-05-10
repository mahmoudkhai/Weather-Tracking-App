package com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase

import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest

interface FiveDaysForecastScreenContract {
    sealed interface Intent {
        data class GetFiveDaysForecast(val fiveDaysForecast: WeatherRequest) : Intent
    }
}