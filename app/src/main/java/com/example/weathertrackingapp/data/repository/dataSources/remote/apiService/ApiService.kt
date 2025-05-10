package com.example.weathertrackingapp.data.repository.dataSources.remote.apiService

import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import kotlin.reflect.KClass

interface ApiService {
    /**
     * Sends a GET request to the specified API and returns the response as the specified type.
     *
     * @param weatherRequest The weather request containing parameters for the API call.
     * @param responseType The class of the response type.
     * @param baseUrl The base URL of the API.
     * @param startDate The start date for the data range.
     * @param endDate The end date for the data range.
     * @param unitGroup The unit system for the response data (default is "metric").
     * @param include The specific data to include in the response (default is "current").
     * @return The parsed response of the specified type.
     */
    fun <ResponseType : Any> get(
        weatherRequest: WeatherRequest,
        responseType: KClass<ResponseType>,
        baseUrl: String,
        startDate: String,
        endDate: String,
        apiKey:String,
        unitGroup: String = "metric",
        include: String = "current",
    ): ResponseType
}