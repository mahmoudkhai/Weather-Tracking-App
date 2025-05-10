package com.example.weathertrackingapp.data.fake

import com.example.weathertrackingapp.data.dto.CurrentConditionDto
import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.data.dto.WholeDayWeatherDto
import com.example.weathertrackingapp.domain.repository.dataSources.local.WeatherLocalDS

class MockedLocalDs : WeatherLocalDS {

    override fun cacheCurrentWeatherDto(currentWeatherEntity: CurrentWeatherDto) {
        return if (localCallShouldFail) throw Exception()
        else Unit

    }

    override fun getCurrentWeatherDto(): CurrentWeatherDto {
        return if (localCallShouldFail) throw Exception()
        else CurrentWeatherDto(
            currentConditions = CurrentConditionDto(),
            days = WholeDayWeatherDto()
        )
    }

    override fun cacheFiveDaysForecastDto(fiveDaysForecastDto: FiveDaysForecastDto) {
        return if (localCallShouldFail) throw Exception()
        else Unit

    }

    override fun getFiveDaysForecastDto(): FiveDaysForecastDto {
        return if (localCallShouldFail) throw Exception()
        else FiveDaysForecastDto()
    }

    var localCallShouldFail: Boolean = false
}