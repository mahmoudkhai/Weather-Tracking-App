package com.example.weathertrackingapp.presentation.fragments.currentWeather

import com.example.weathertrackingapp.common.observerPattern.Subscriber
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathertrackingapp.presentation.fragments.base.BaseViewModel
import com.example.weathertrackingapp.presentation.presentationUtil.toCurrentWeather
import com.example.weathertrackingapp.presentation.presentationUtil.UiEvent

class CurrentWeatherViewModel(private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase) :
    BaseViewModel<UiEvent>(), Subscriber<DomainState<CurrentWeatherEntity>> {

    override val subscribers = mutableSetOf<Subscriber<UiEvent>>()

    init {
        getCurrentWeatherUseCase.registerSubscriber(this)
    }

    fun processUserIntent(intent: CurrentWeatherScreenContract.Intent) =
        when (intent) {
            is CurrentWeatherScreenContract.Intent.GetCurrentWeather -> {
                getCurrentWeatherUseCase(intent.weatherRequest)
            }
        }

    override fun onUpdate(domainState: DomainState<CurrentWeatherEntity>) = when (domainState) {
        is DomainState.Loading -> notifyAllSubscribers(UiEvent.ShowLoading(domainState.isLoading))
        is DomainState.SuccessWithFreshData<CurrentWeatherEntity> -> notifyAllSubscribers(
            UiEvent.SuccessWithFreshData(domainState.data.toCurrentWeather())
        )

        is DomainState.FailureWithCachedData -> {
            if (domainState.cachedData != null) {
                notifyAllSubscribers(UiEvent.ShowError(domainState.exception))
                notifyAllSubscribers(UiEvent.SuccessWithCachedData(domainState.cachedData.toCurrentWeather()))
            } else
                notifyAllSubscribers(UiEvent.ShowError(domainState.exception))
        }
    }

}