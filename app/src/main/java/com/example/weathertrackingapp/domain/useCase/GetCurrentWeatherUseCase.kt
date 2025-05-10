package com.example.weathertrackingapp.domain.useCase

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.observerPattern.Subscriber
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
) : BaseUseCase<DomainState<CurrentWeatherEntity>>() {

    override val subscribers = mutableSetOf<Subscriber<DomainState<CurrentWeatherEntity>>>()

    operator fun invoke(weatherRequest: WeatherRequest) {
        Log.d(TAG, "currentWeather useCase got weather request = $weatherRequest")
        notifyAllSubscribers(DomainState.Loading(true))
        notifyAllSubscribers(weatherRepository.getCurrentWeather(weatherRequest))
        notifyAllSubscribers(DomainState.Loading(false))
    }
}
