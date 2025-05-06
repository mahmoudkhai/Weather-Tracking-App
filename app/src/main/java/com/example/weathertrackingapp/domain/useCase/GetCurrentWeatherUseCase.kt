package com.example.weathertrackingapp.domain.useCase

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentConditions
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
) : BaseUseCase<DomainState<CurrentConditions>>() {

    override val observers = mutableSetOf<Observer<DomainState<CurrentConditions>>>()

    operator fun invoke(weatherRequest: WeatherRequest) {
        Log.d(TAG, "invoke: getting current weather for $weatherRequest")
        try {
            notifyObservers(DomainState.Loading(true))
            val currentConditions = weatherRepository.getCurrentWeather(weatherRequest)
            notifyObservers(DomainState.Success(currentConditions))
        } catch (e: CustomException) {
            Log.e(TAG, "invoke: error getting current weather", e)
            notifyObservers(DomainState.Failure(e))
        } finally {
            notifyObservers(DomainState.Loading(false))
        }
    }
}