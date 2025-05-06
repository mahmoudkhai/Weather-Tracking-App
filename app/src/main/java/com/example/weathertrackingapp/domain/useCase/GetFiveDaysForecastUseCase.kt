package com.example.weathertrackingapp.domain.useCase

import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.common.customState.DomainState
import com.example.weathertrackingapp.common.observerPattern.Observable
import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.domain.model.requestModels.CurrentWeatherRequest
import com.example.weathertrackingapp.domain.model.responseModels.FiveDaysForecast
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class GetFiveDaysForecastUseCase(private val weatherRepository: WeatherRepository) :
    Observable<DomainState<FiveDaysForecast>> {


    operator fun invoke(
        currentWeatherRequest: CurrentWeatherRequest,
    ) {
        try {
            notifyObservers(DomainState.Loading(true))
            val fiveDaysForecast = weatherRepository.getFiveDaysForecast(currentWeatherRequest)
            notifyObservers(DomainState.Success(fiveDaysForecast))
        } catch (e: CustomException) {
            notifyObservers(DomainState.Failure(e))
        } finally {
            notifyObservers(DomainState.Loading(false))
        }
    }

    private val observers = mutableListOf<Observer<DomainState<FiveDaysForecast>>>()


    override fun registerObserver(observer: Observer<DomainState<FiveDaysForecast>>) {
        observers.add(observer)
    }

    override fun unregisterObserver(observer: Observer<DomainState<FiveDaysForecast>>) {
        observers.remove(observer)
    }

    override fun notifyObservers(newState: DomainState<FiveDaysForecast>) {
        observers.forEach { it.onUpdate(newState) }
    }
}