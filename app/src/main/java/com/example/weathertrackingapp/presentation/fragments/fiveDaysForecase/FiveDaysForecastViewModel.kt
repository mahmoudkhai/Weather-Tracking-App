package com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase

import com.example.weathertrackingapp.common.observerPattern.Subscriber
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.useCase.GetFiveDaysForecastUseCase
import com.example.weathertrackingapp.presentation.fragments.base.BaseViewModel
import com.example.weathertrackingapp.presentation.model.extMappers.toFiveDaysForecast
import com.example.weathertrackingapp.presentation.presentationUtil.UiEvent

class FiveDaysForecastViewModel(
    private val getFiveDaysForecastUseCase: GetFiveDaysForecastUseCase,
) : BaseViewModel<UiEvent>(), Subscriber<DomainState<FiveDaysForecastEntity>> {

    override val subscribers = mutableSetOf<Subscriber<UiEvent>>()

    init {
        getFiveDaysForecastUseCase.registerSubscriber(this)
    }

    fun processUserIntent(intent: FiveDaysForecastScreenContract.Intent) = when (intent) {
        is FiveDaysForecastScreenContract.Intent.GetFiveDaysForecast -> getFiveDaysForecastUseCase(
            intent.fiveDaysForecast
        )
    }

    override fun onUpdate(domainState: DomainState<FiveDaysForecastEntity>) = when (domainState) {
        is DomainState.Loading -> notifyAllSubscribers(UiEvent.ShowLoading(domainState.isLoading))
        is DomainState.SuccessWithFreshData<FiveDaysForecastEntity> -> notifyAllSubscribers(
            UiEvent.SuccessWithFreshData(domainState.data.toFiveDaysForecast())
        )

        is DomainState.FailureWithCachedData -> {
            if (domainState.cachedData != null) {
                notifyAllSubscribers(UiEvent.ShowError(domainState.exception))
                notifyAllSubscribers(UiEvent.SuccessWithCachedData(domainState.cachedData.toFiveDaysForecast()))
            } else
                notifyAllSubscribers(UiEvent.ShowError(domainState.exception))
        }
    }
}