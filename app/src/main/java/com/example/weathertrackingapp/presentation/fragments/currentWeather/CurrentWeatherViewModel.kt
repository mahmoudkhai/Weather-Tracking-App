package com.example.weathertrackingapp.presentation.fragments.currentWeather

import com.example.weathertrackingapp.common.customState.DomainState
import com.example.weathertrackingapp.domain.model.responseModels.CurrentConditions
import com.example.weathertrackingapp.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathertrackingapp.common.observerPattern.Observable
import com.example.weathertrackingapp.common.observerPattern.Observer

class CurrentWeatherViewModel(private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase) :
    Observer<DomainState<CurrentConditions>>, Observable<UiEvent> {

    init {
        getCurrentWeatherUseCase.registerObserver(this)
    }

    private val observers = mutableSetOf<Observer<UiEvent>>()

    fun processUserIntent(userIntent: UserIntent) =
        when (userIntent) {
            is UserIntent.GetCurrentWeather -> {
                getCurrentWeatherUseCase(userIntent.currentWeatherRequest)
            }
        }

    override fun onUpdate(domainState: DomainState<CurrentConditions>) = when (domainState) {
        is DomainState.Loading -> notifyObservers(UiEvent.ShowLoading(domainState.isLoading))
        is DomainState.Success<CurrentConditions> -> notifyObservers(UiEvent.Success(domainState.data))
        is DomainState.Failure -> notifyObservers(UiEvent.ShowError(domainState.exception))
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