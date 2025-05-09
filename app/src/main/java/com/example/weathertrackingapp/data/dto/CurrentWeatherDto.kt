package com.example.weathertrackingapp.data.dto

data class CurrentWeatherDto(
    val queryCost: Int? = null,
    val resolvedAddress: String? = null,
    val timeZone: String? = null,
    val address: String? = null,
    val currentConditions: CurrentConditionDto? = null,
    val days: WholeDayWeatherDto? = null,
)

data class CurrentConditionDto(
    val cloudcover: Double? = null,
    val conditions: String? = null,
    val datetime: String? = null,
    val datetimeEpoch: Int? = null,
    val dew: Double? = null,
    val feelslike: Double? = null,
    val humidity: Double? = null,
    val icon: String? = null,
    val moonphase: Double? = null,
    val precipprob: Double? = null,
    val pressure: Double? = null,
    val snow: Double? = null,
    val snowdepth: Double? = null,
    val solarenergy: Double? = null,
    val solarradiation: Double? = null,
    val source: String? = null,
    val sunrise: String? = null,
    val sunriseEpoch: Int? = null,
    val sunset: String? = null,
    val sunsetEpoch: Int? = null,
    val temp: Double? = null,
    val uvindex: Double? = null,
    val visibility: Double? = null,
    val winddir: Double? = null,
    val windspeed: Double? = null,
)
