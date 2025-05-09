package com.example.weathertrackingapp.domain.repository.dataSources.local

import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto

interface WeatherLocalDS {
    fun cacheCurrentWeatherDto(currentWeatherEntity: CurrentWeatherDto)
    fun getCurrentWeatherDto(): CurrentWeatherDto
    fun cacheFiveDaysForecastDto(fiveDaysForecastDto: FiveDaysForecastDto)
    fun getFiveDaysForecastDto(): FiveDaysForecastDto
}