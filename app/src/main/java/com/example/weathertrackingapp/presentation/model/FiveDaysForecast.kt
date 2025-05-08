package com.example.weathertrackingapp.presentation.model

import com.example.weathertrackingapp.domain.entity.responseEntities.WholeDayWeatherEntity

data class FiveDaysForecast(
    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val days: List<WholeDayWeather>,
    )
