package com.example.weathertrackingapp.domain.model.requestModels

data class CurrentWeatherRequest(
    val latLong: LatLong,
    val language: String = "en",
)
