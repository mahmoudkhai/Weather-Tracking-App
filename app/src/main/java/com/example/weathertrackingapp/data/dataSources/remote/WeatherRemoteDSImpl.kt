package com.example.weathertrackingapp.data.dataSources.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.data.constants.ApiKeyProvider
import com.example.weathertrackingapp.data.dataSources.remote.apiService.ApiService
import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.dataSources.remote.WeatherRemoteDS
import com.example.weathertrackingapp.domain.model.WeatherRequest
import com.google.android.gms.common.internal.Constants
import java.time.LocalDate
import java.time.LocalDateTime

class WeatherRemoteDSImpl(private val api: ApiService) : WeatherRemoteDS {

    @RequiresApi(Build.VERSION_CODES.O)
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