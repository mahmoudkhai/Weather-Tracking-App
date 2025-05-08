package com.example.weathertrackingapp.presentation.model

data class WholeDayWeather(
    val description: String,
    val conditions: String,
    val datetime: String,
    val feelslike: Double,
    val humidity: Double,
    val icon: String,
    val precip: Double,
    val pressure: Double,
    val sunrise: String,
    val temp: Double,
    val windspeed: Double,
)
