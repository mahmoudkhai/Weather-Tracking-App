package com.example.weathertrackingapp.domain.useCase

import com.example.weathertrackingapp.common.observerPattern.Subscriber
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.repository.WeatherRepository

/**
 * Use case for fetching five days forecast from the repository.
 * This class interacts with the WeatherRepository to get weather data and notifies its subscribers of the result.
 *
 * @param weatherRepository The repository responsible for fetching weather data.
 *
 * **Important:** Ensure that you register and unregister your observers when using this use case to avoid memory leaks.
 */
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