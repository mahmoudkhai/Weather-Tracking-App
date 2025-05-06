package com.example.weathertrackingapp.data.dataSources.remote.apiService

import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import kotlin.reflect.KClass

interface ApiService {
    fun <ResponseType : Any> get(
        weatherRequest: WeatherRequest,
        responseType: KClass<ResponseType>,
        baseUrl: String,
        startDate: String,
        endDate: String,
        apiKey: String,
        unitGroup: String = "metric",
        include: String = "current",
    ): ResponseType
}