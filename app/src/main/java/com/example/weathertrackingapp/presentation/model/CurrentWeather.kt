package com.example.weathertrackingapp.presentation.model

data class CurrentWeather(
    val queryCost: Int? = null,
    val resolvedAddress: String? = null,
    val timeZone: String? = null,
    val address: String? = null,
    val currentConditions: CurrentConditions,
    val wholeDayWeather: WholeDayWeather,
)

data class CurrentConditions(
    val pressure:Double?=null,
    val dateTime: String? = null,
    val temperature: Double? = null,
    val feelsLike: Double? = null,
    val conditions: String? = null,
    val icon: String? = null,
    val humidity: Double? = null,
    val cloudCover: Double? = null,
    val windSpeed: Double? = null,
    val uvIndex: Double? = null,
    val sunrise: String? = null,
    val sunset: String? = null,
)