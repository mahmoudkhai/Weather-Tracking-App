package com.example.weathertrackingapp.data.repository

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.data.mappers.CurrentConditionsMapper
import com.example.weathertrackingapp.domain.repository.dataSources.remote.WeatherRemoteDS
import com.example.weathertrackingapp.domain.model.responseModels.CurrentConditions
import com.example.weathertrackingapp.domain.model.responseModels.FiveDaysForecast
import com.example.weathertrackingapp.domain.model.requestModels.CurrentWeatherRequest
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class WeatherRepositoryImpl(private val weatherRemoteDS: WeatherRemoteDS) :
    WeatherRepository {
    override fun getCurrentWeather(currentWeatherRequest: CurrentWeatherRequest): CurrentConditions {
        Log.d(TAG, "getCurrentWeather: now in the repository impl")
        return CurrentConditionsMapper.dtoToDomain(
            weatherRemoteDS.getCurrentWeather(
                currentWeatherRequest
            )
        )
    }

    override fun getFiveDaysForecast(currentWeatherRequest: CurrentWeatherRequest): FiveDaysForecast {
        TODO("Not yet implemented")
    }
}