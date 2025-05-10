package com.example.weathertrackingapp.domain.fake

import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class MockedRepository : WeatherRepository {
    var shouldFail: Boolean = false
    var exceptions = listOf<CustomException>()
    var fiveDaysForecastCachedData: FiveDaysForecastEntity? = null

    override fun getCurrentWeather(weatherRequest: WeatherRequest): DomainState<CurrentWeatherEntity> {
        return if (shouldFail) {
            DomainState.FailureWithCachedData(listOf(CustomException.NetworkException.NoInternetConnection))
        } else {
            DomainState.SuccessWithFreshData(CurrentWeatherEntity())
        }
    }

    override fun getFiveDaysForecast(weatherRequest: WeatherRequest): DomainState<FiveDaysForecastEntity> {
        return if (shouldFail) {
            DomainState.FailureWithCachedData(exceptions, cachedData = fiveDaysForecastCachedData)
        } else {
            DomainState.SuccessWithFreshData(FiveDaysForecastEntity())
        }
    }

    companion object {
    }
}