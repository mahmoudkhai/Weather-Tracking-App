package com.example.weathertrackingapp.domain.entity.responseEntities

data class FiveDaysForecastEntity(
    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val days: List<WholeDayWeatherEntity>,
)
