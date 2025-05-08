package com.example.weathertrackingapp.domain.useCase

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
        notifyAllSubscribers(DomainState.Loading(true))
        notifyAllSubscribers(weatherRepository.getCurrentWeather(weatherRequest))
        notifyAllSubscribers(DomainState.Loading(false))
    }
}
