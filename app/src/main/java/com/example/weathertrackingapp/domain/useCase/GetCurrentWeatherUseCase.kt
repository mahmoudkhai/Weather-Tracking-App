package com.example.weathertrackingapp.domain.useCase

import android.util.Log
import com.example.weathertrackingapp.common.customState.ResultState
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.weatherException.CustomException
import com.example.weathertrackingapp.domain.model.CurrentConditions
import com.example.weathertrackingapp.domain.model.WeatherRequest
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
) {

    private val observers = mutableSetOf<UseCaseObserver<CurrentConditions>>()

    operator fun invoke(weatherRequest: WeatherRequest) {
        Log.d(TAG, "invoke: getting current weather for $weatherRequest")
        try {
            notifyObservers(ResultState.IsLoading(true))
            val currentConditions = weatherRepository.getCurrentWeather(weatherRequest)
            notifyObservers(ResultState.Success(currentConditions))
        } catch (e: CustomException) {
            Log.e(TAG, "invoke: error getting current weather", e)
            notifyObservers(ResultState.Failure(e))
        }finally {
            notifyObservers(ResultState.IsLoading(false))
        }
    }

    private fun notifyObservers(newState: ResultState<CurrentConditions>) {
        observers.forEach { it.onUpdate(newState) }
    }


    fun registerObserver(observer: UseCaseObserver<CurrentConditions>) {
        observers.add(observer)
    }

    fun unregisterObserver(observer: UseCaseObserver<CurrentConditions>) {
        observers.remove(observer)
    }


}