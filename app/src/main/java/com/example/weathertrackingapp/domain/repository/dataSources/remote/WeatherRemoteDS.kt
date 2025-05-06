package com.example.weathertrackingapp.domain.repository.dataSources.remote

import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.domain.model.requestModels.CurrentWeatherRequest

interface WeatherRemoteDS {
    fun getCurrentWeather(currentWeatherRequest: CurrentWeatherRequest): CurrentConditionsDto
    fun getFiveDaysForecast(currentWeatherRequest: CurrentWeatherRequest): List<FiveDaysForecastDto>
}