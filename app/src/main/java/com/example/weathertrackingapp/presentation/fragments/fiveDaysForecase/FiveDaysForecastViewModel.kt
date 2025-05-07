package com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase

import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecast
import com.example.weathertrackingapp.domain.useCase.GetFiveDaysForecastUseCase
import com.example.weathertrackingapp.presentation.fragments.base.BaseViewModel
import com.example.weathertrackingapp.presentation.presentationUtil.UiEvent

class FiveDaysForecastViewModel(
    private val getFiveDaysForecastUseCase: GetFiveDaysForecastUseCase,
) : BaseViewModel<UiEvent>(), Observer<DomainState<FiveDaysForecast>> {

    override val observers = mutableSetOf<Observer<UiEvent>>()

    init {
        getFiveDaysForecastUseCase.registerObserver(this)
    }

    fun processUserIntent(intent: FiveDaysForecastScreenContract.Intent) = when (intent) {
        is FiveDaysForecastScreenContract.Intent.GetFiveDaysForecast -> getFiveDaysForecastUseCase(
            intent.fiveDaysForecast
        )
    }

    override fun onUpdate(domainState: DomainState<FiveDaysForecast>) = when (domainState) {
        is DomainState.Loading -> notifyObservers(UiEvent.ShowLoading(domainState.isLoading))
        is DomainState.Success<FiveDaysForecast> -> notifyObservers(UiEvent.Success(domainState.data))
        is DomainState.Failure -> notifyObservers(UiEvent.ShowError(domainState.exception))
    }
}