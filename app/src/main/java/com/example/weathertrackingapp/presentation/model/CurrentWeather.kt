package com.example.weathertrackingapp.presentation.model

data class CurrentWeather(
    val temperature: Double,
    val feelsLike: Double,
    val conditions: String,
    val icon: String,
    val humidity: Double,
    val cloudCover: Double,
    val windSpeed: Double,
    val uvIndex: Double,
    val sunrise: String,
    val sunset: String
)
