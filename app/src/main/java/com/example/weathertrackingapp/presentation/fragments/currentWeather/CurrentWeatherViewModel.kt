package com.example.weathertrackingapp.presentation.fragments.currentWeather

import com.example.weathertrackingapp.common.customState.DataState
import com.example.weathertrackingapp.domain.model.CurrentConditions
import com.example.weathertrackingapp.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathertrackingapp.common.observerPattern.Observable
import com.example.weathertrackingapp.common.observerPattern.Observer

class CurrentWeatherViewModel(private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase) :
    Observer<DataState<CurrentConditions>>, Observable<UiEvent> {

    init {
        getCurrentWeatherUseCase.registerObserver(this)
    }

    private val observers = mutableSetOf<Observer<UiEvent>>()

    fun processUserIntent(userIntent: UserIntent) =
        when (userIntent) {
            is UserIntent.GetCurrentWeather -> {
                getCurrentWeatherUseCase(userIntent.weatherRequest)
            }
        }

    override fun onUpdate(dataState: DataState<CurrentConditions>) = when (dataState) {
        is DataState.Loading -> notifyObservers(UiEvent.ShowLoading(dataState.isLoading))
        is DataState.Success<CurrentConditions> -> notifyObservers(UiEvent.Success(dataState.data))
        is DataState.Failure -> notifyObservers(UiEvent.ShowError(dataState.exception))
    }

    override fun registerObserver(observer: Observer<UiEvent>) {
        observers.add(observer)
    }

    override fun unregisterObserver(observer: Observer<UiEvent>) {
        observers.remove(observer)
    }

    override fun notifyObservers(newEvent: UiEvent) {
        observers.forEach { it.onUpdate(newEvent) }
    }

    fun onDestroy() = getCurrentWeatherUseCase.unregisterObserver(this)

}