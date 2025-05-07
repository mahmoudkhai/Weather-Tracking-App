package com.example.weathertrackingapp.domain.repository.dataSources.local

import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeather
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecast

interface WeatherLocalDS {
    fun cacheCurrentWeather(currentWeather: CurrentWeather)
    fun getCurrentWeather(): CurrentWeather
    fun cacheFiveDaysForecast(fiveDaysForecast:FiveDaysForecast)
    fun getFiveDaysForecast(): FiveDaysForecast
}