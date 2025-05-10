package com.example.weathertrackingapp.domain.entity.responseEntities

data class WholeDayWeatherEntity(
    val description: String? = null,
    val conditions: String? = null,
    val datetime: String? = null,
    val feelslike: Double? = null,
    val humidity: Double? = null,
    val icon: String? = null,
    val precip: Double? = null,
    val pressure: Double? = null,
    val sunrise: String? = null,
    val temp: Double? = null,
    val windspeed: Double? = null,
)