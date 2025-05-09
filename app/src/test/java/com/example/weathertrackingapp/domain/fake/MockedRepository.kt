package com.example.weathertrackingapp.domain.fake

import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.WholeDayWeatherEntity
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class MockedRepository : WeatherRepository {


    override fun getCurrentWeather(weatherRequest: WeatherRequest): DomainState<CurrentWeatherEntity> {
        passedWeatherRequest = weatherRequest
        return DomainState.SuccessWithFreshData(CurrentWeatherEntity(wholeDayWeather = WholeDayWeatherEntity()))
    }

    override fun getFiveDaysForecast(weatherRequest: WeatherRequest): DomainState<FiveDaysForecastEntity> {
        TODO("Not yet implemented")
    }

    companion object {
        var passedWeatherRequest: WeatherRequest? = null
    }
}