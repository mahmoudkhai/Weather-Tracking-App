package com.example.weathertrackingapp.data.dataSources.remote.apiService

import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.model.WeatherRequest

interface ApiService {
    fun getCurrentWeatherConditions(
        weatherRequest: WeatherRequest,
        baseUrl: String,
        startDate: String,
        endDate: String,
        apiKey: String,
        unitGroup: String = "metric",
        include: String = "current",
    ): CurrentConditionsDto

}
