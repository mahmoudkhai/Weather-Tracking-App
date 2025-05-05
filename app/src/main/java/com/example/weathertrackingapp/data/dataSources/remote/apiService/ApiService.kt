package com.example.weathertrackingapp.data.dataSources.remote.apiService

import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.model.WeatherRequest

interface ApiService {
    fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentConditionsDto

}
