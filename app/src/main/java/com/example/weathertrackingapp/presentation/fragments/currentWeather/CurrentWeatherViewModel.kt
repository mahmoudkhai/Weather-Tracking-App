package com.example.weathertrackingapp.presentation.fragments.currentWeather

import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentConditions
import com.example.weathertrackingapp.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.presentation.fragments.base.BaseViewModel
import com.example.weathertrackingapp.presentation.presentationUtil.UiEvent

class CurrentWeatherViewModel(private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase) :
    BaseViewModel<UiEvent>(), Observer<DomainState<CurrentConditions>> {

    override val observers = mutableSetOf<Observer<UiEvent>>()

    init {
        getCurrentWeatherUseCase.registerObserver(this)
    }

    fun processUserIntent(intent: CurrentWeatherScreenContract.Intent) =
        when (intent) {
            is CurrentWeatherScreenContract.Intent.GetCurrentWeather -> {
                getCurrentWeatherUseCase(intent.weatherRequest)
            }
        }

    override fun onUpdate(domainState: DomainState<CurrentConditions>) = when (domainState) {
        is DomainState.Loading -> notifyObservers(UiEvent.ShowLoading(domainState.isLoading))
        is DomainState.Success<CurrentConditions> -> notifyObservers(UiEvent.Success(domainState.data))
        is DomainState.Failure -> notifyObservers(UiEvent.ShowError(domainState.exception))
    }

}