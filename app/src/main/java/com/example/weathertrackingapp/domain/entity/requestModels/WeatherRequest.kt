package com.example.weathertrackingapp.domain.entity.requestModels

data class WeatherRequest(
    val latLong: LatLong,
    val language: String = "en",
)
