package com.example.weathertrackingapp.domain.useCase

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecast
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class GetFiveDaysForecastUseCase(private val weatherRepository: WeatherRepository) :
    BaseUseCase<DomainState<FiveDaysForecast>>() {

    override val observers = mutableSetOf<Observer<DomainState<FiveDaysForecast>>>()

    operator fun invoke(
        weatherRequest: WeatherRequest,
    ) {
        try {
            notifyObservers(DomainState.Loading(true))
            val fiveDaysForecast = weatherRepository.getFiveDaysForecast(weatherRequest)
            Log.d(TAG, "now five days weather looks like this \n $fiveDaysForecast")
            notifyObservers(DomainState.Success(fiveDaysForecast))
        } catch (e: CustomException) {
            notifyObservers(DomainState.Failure(e))
        } finally {
            notifyObservers(DomainState.Loading(false))
        }
    }


}