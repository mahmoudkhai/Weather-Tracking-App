package com.example.weathertrackingapp.domain.entity.responseEntities

import com.example.weathertrackingapp.presentation.model.WholeDayWeather

data class CurrentWeatherEntity(
    val queryCost: Int? = null,
    val resolvedAddress: String? = null,
    val timeZone: String? = null,
    val address: String? = null,
    val currentConditions: CurrentConditionsEntity? = null,
    val wholeDayWeather: WholeDayWeatherEntity = WholeDayWeatherEntity()
)

data class CurrentConditionsEntity(
    val dateTime: String? = null,
    val pressure:Double?=null,
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