package com.example.weathertrackingapp.domain.dataSources.remote

import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.model.WeatherRequest

interface WeatherRemoteDS {
    fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentConditionsDto
}