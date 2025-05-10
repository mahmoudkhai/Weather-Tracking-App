package com.example.weathertrackingapp.data.fake

import com.example.weathertrackingapp.data.dto.CurrentConditionDto
import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.data.dto.WholeDayWeatherDto
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.repository.dataSources.remote.WeatherRemoteDS
import java.io.IOException

class MockedRemoteDS : WeatherRemoteDS {
    override fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentWeatherDto {
        return if (remoteCallShouldFail)
            throw IOException()
        else CurrentWeatherDto(currentConditions = CurrentConditionDto(), days = WholeDayWeatherDto())
    }

    override fun getFiveDaysForecast(weatherRequest: WeatherRequest): FiveDaysForecastDto {
        return if (remoteCallShouldFail)
            throw IOException()
        else FiveDaysForecastDto()
    }

        var remoteCallShouldFail: Boolean = false
}