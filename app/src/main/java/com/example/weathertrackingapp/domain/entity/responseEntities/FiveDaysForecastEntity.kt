package com.example.weathertrackingapp.domain.entity.responseEntities

data class FiveDaysForecastEntity(
    val resolvedAddress: String? = null,
    val address: String? = null,
    val timezone: String? = null,
    val days: List<WholeDayWeatherEntity>? = null,
)