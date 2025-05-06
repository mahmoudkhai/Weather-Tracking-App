package com.example.weathertrackingapp.domain.model.requestModels

data class FiveDaysForecastRequest(
    val latLong: LatLong,
    val language: String = "en",
)
