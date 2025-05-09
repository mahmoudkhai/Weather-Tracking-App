package com.example.weathertrackingapp.presentation.model

import com.example.weathertrackingapp.domain.entity.responseEntities.WholeDayWeatherEntity

data class FiveDaysForecast(
    val resolvedAddress: String? = null,
    val address: String? = null,
    val timezone: String? = null,
    val days: List<WholeDayWeather>? = null,
)

data class WholeDayWeather(
    val description: String? = null,
    val conditions: String? = null,
    val datetime: String? = null,
    val feelslike: Double? = null,
    val humidity: Double? = null,
    val icon: String? = null,
    val precip: Double? = null,
    val pressure: Double? = null,
    val sunrise: String? = null,
    val temp: Double? = null,
    val windspeed: Double? = null,
)