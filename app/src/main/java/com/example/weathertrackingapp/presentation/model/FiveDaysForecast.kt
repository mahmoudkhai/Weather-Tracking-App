package com.example.weathertrackingapp.presentation.model

import com.example.weathertrackingapp.domain.entity.responseEntities.WholeDayWeatherEntity

data class FiveDaysForecast(
    val resolvedAddress: String? = null,
    val address: String? = null,
    val timezone: String? = null,
    val days: List<WholeDayWeather>? = null,
)

