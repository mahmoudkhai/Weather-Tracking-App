package com.example.weathertrackingapp.domain.useCase

import com.example.weathertrackingapp.common.observerPattern.Subscriber
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class GetFiveDaysForecastUseCase(private val weatherRepository: WeatherRepository) :
    BaseUseCase<DomainState<FiveDaysForecastEntity>>() {

    override val subscribers = mutableSetOf<Subscriber<DomainState<FiveDaysForecastEntity>>>()

    operator fun invoke(
        weatherRequest: WeatherRequest,
    ) {
        notifyAllSubscribers(DomainState.Loading(true))
        notifyAllSubscribers(weatherRepository.getFiveDaysForecast(weatherRequest))
        notifyAllSubscribers(DomainState.Loading(false))
    }
}