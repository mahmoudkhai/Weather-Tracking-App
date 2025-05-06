package com.example.weathertrackingapp.data.repository

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.data.mappers.CurrentConditionsMapper
import com.example.weathertrackingapp.data.mappers.FiveDaysForecastMapper
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentConditions
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecast
import com.example.weathertrackingapp.domain.repository.WeatherRepository
import com.example.weathertrackingapp.domain.repository.dataSources.remote.WeatherRemoteDS

class WeatherRepositoryImpl(private val weatherRemoteDS: WeatherRemoteDS) :
    WeatherRepository {
    override fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentConditions {
        Log.d(TAG, "getCurrentWeather: now in the repository impl to get current weather")
        return CurrentConditionsMapper.dtoToDomain(
            weatherRemoteDS.getCurrentWeather(
                weatherRequest
            )
        )
    }

    override fun getFiveDaysForecast(weatherRequest: WeatherRequest): FiveDaysForecast {
        Log.d(TAG, "getCurrentWeather: now in the repository impl to get five days weather")
        return FiveDaysForecastMapper.dtoToDomain(weatherRemoteDS.getFiveDaysForecast(weatherRequest))
    }
}