package com.example.weathertrackingapp.domain.repository.dataSources.remote

import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.data.dto.DayForecastDto
import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest

interface WeatherRemoteDS {
    fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentConditionsDto
    fun getFiveDaysForecast(weatherRequest: WeatherRequest): FiveDaysForecastDto
}