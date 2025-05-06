package com.example.weathertrackingapp.domain.repository

import com.example.weathertrackingapp.domain.model.responseModels.CurrentConditions
import com.example.weathertrackingapp.domain.model.responseModels.FiveDaysForecast
import com.example.weathertrackingapp.domain.model.requestModels.CurrentWeatherRequest

interface WeatherRepository {
    fun getCurrentWeather(currentWeatherRequest: CurrentWeatherRequest): CurrentConditions
    fun getFiveDaysForecast(currentWeatherRequest: CurrentWeatherRequest): FiveDaysForecast
}