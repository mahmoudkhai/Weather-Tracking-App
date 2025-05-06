package com.example.weathertrackingapp.domain.repository

import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentConditions
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecast
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest

interface WeatherRepository {
    fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentConditions
    fun getFiveDaysForecast(weatherRequest: WeatherRequest): FiveDaysForecast
}