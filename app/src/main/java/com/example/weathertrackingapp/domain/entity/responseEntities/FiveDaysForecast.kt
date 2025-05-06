package com.example.weathertrackingapp.domain.entity.responseEntities

data class FiveDaysForecast(
    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val days: List<DayForecast>,
)
