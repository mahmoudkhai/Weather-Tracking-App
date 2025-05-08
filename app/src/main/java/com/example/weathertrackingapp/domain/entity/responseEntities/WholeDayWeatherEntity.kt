package com.example.weathertrackingapp.domain.entity.responseEntities

data class WholeDayWeatherEntity(
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
