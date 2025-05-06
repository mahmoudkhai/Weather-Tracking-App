package com.example.weathertrackingapp.domain.repository

import com.example.weathertrackingapp.domain.model.CurrentConditions
import com.example.weathertrackingapp.domain.model.WeatherRequest

interface WeatherRepository {
    fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentConditions
}