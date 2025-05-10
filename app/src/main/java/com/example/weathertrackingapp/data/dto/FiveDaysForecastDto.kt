package com.example.weathertrackingapp.data.dto

data class FiveDaysForecastDto(
    val resolvedAddress: String?=null,
    val address: String?=null,
    val timezone: String?=null,
    val days: List<WholeDayWeatherDto>?=null,
)