package com.example.weathertrackingapp.domain.repository

import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest

interface WeatherRepository {
    fun getCurrentWeather(weatherRequest: WeatherRequest): DomainState<CurrentWeatherEntity>
    fun getFiveDaysForecast(weatherRequest: WeatherRequest): DomainState<FiveDaysForecastEntity>
}