package com.example.weathertrackingapp.domain.model

data class WeatherRequest(
    val latLong: LatLong,
    val language: String = "en",
)
