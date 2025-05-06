package com.example.weathertrackingapp.domain.useCase

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.customState.DataState
import com.example.weathertrackingapp.common.observerPattern.Observable
import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.common.weatherException.CustomException
import com.example.weathertrackingapp.domain.model.CurrentConditions
import com.example.weathertrackingapp.domain.model.WeatherRequest
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
) : Observable<DataState<CurrentConditions>> {

    private val observers = mutableSetOf<Observer<DataState<CurrentConditions>>>()

    operator fun invoke(weatherRequest: WeatherRequest) {
        Log.d(TAG, "invoke: getting current weather for $weatherRequest")
        try {
            notifyObservers(DataState.IsLoading(true))
            val currentConditions = weatherRepository.getCurrentWeather(weatherRequest)
            notifyObservers(DataState.Success(currentConditions))
        } catch (e: CustomException) {
            Log.e(TAG, "invoke: error getting current weather", e)
            notifyObservers(DataState.Failure(e))
        } finally {
            notifyObservers(DataState.IsLoading(false))
        }
    }

    override fun notifyObservers(newState: DataState<CurrentConditions>) {
        observers.forEach { it.onUpdate(newState) }
    }

    override fun registerObserver(observer: Observer<DataState<CurrentConditions>>) {
        observers.add(observer)
    }

    override fun unregisterObserver(observer: Observer<DataState<CurrentConditions>>) {
        observers.remove(observer)
    }


}