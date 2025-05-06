package com.example.weathertrackingapp.domain.useCase

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.customState.DomainState
import com.example.weathertrackingapp.common.observerPattern.Observable
import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.domain.model.responseModels.CurrentConditions
import com.example.weathertrackingapp.domain.model.requestModels.CurrentWeatherRequest
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
) : Observable<DomainState<CurrentConditions>> {

    private val observers = mutableSetOf<Observer<DomainState<CurrentConditions>>>()

    operator fun invoke(currentWeatherRequest: CurrentWeatherRequest) {
        Log.d(TAG, "invoke: getting current weather for $currentWeatherRequest")
        try {
            notifyObservers(DomainState.Loading(true))
            val currentConditions = weatherRepository.getCurrentWeather(currentWeatherRequest)
            notifyObservers(DomainState.Success(currentConditions))
        } catch (e: CustomException) {
            Log.e(TAG, "invoke: error getting current weather", e)
            notifyObservers(DomainState.Failure(e))
        } finally {
            notifyObservers(DomainState.Loading(false))
        }
    }

    override fun notifyObservers(newState: DomainState<CurrentConditions>) {
        observers.forEach { it.onUpdate(newState) }
    }

    override fun registerObserver(observer: Observer<DomainState<CurrentConditions>>) {
        observers.add(observer)
    }

    override fun unregisterObserver(observer: Observer<DomainState<CurrentConditions>>) {
        observers.remove(observer)
    }


}