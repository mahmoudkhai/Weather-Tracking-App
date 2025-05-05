package com.example.weathertrackingapp.data.dataSources.remote

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.data.dataSources.remote.apiService.ApiService
import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.dataSources.remote.WeatherRemoteDS
import com.example.weathertrackingapp.domain.model.WeatherRequest

class WeatherRemoteDSImpl(private val api: ApiService) : WeatherRemoteDS {

    override fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentConditionsDto {
        Log.d(TAG, "getCurrentWeather: from weather remote dataSource")
        return api.getCurrentWeather(weatherRequest)

    }

    companion object {
//        private const val
    }
}