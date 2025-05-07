package com.example.weathertrackingapp.domain.repository

import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeather
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecast
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest

interface WeatherRepository {
    fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentWeather
    fun getFiveDaysForecast(weatherRequest: WeatherRequest): FiveDaysForecast
}