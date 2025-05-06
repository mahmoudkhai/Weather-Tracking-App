package com.example.weathertrackingapp.data.dto

data class FiveDaysForecastDto(
    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val days: List<DayForecastDto>,
)
