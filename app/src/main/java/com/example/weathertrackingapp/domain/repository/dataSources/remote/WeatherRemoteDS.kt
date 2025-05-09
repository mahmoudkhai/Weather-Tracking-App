package com.example.weathertrackingapp.domain.repository.dataSources.remote

import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest

interface WeatherRemoteDS {
    fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentWeatherDto
    fun getFiveDaysForecast(weatherRequest: WeatherRequest): FiveDaysForecastDto
}