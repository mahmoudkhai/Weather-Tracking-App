package com.example.weathertrackingapp.data.dataSources.remote

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.data.constants.ApiKeyProvider
import com.example.weathertrackingapp.data.dataSources.remote.apiService.ApiService
import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.model.WeatherRequest
import com.example.weathertrackingapp.domain.repository.dataSources.remote.WeatherRemoteDS
import java.time.LocalDate

class WeatherRemoteDSImpl(private val api: ApiService) : WeatherRemoteDS {

    override fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentConditionsDto {
        Log.d(TAG, "getCurrentWeather: from weather remote dataSource")
        return api.getCurrentWeatherConditions(
            weatherRequest = weatherRequest,
            baseUrl = BASE_URL,
            apiKey = ApiKeyProvider.API_KEY,
            startDate = LocalDate.now().toString(),
            endDate = LocalDate.now().toString(),
        )
    }

    companion object {
        private const val BASE_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
    }
}