package com.example.weathertrackingapp.data.dataSources.remote.apiService

import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.model.requestModels.CurrentWeatherRequest

interface ApiService {
    fun getCurrentWeatherConditions(
        currentWeatherRequest: CurrentWeatherRequest,
        baseUrl: String,
        startDate: String,
        endDate: String,
        apiKey: String,
        unitGroup: String = "metric",
        include: String = "current",
    ): CurrentConditionsDto

}
