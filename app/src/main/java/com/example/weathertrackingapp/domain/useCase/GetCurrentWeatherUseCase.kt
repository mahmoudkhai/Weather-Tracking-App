package com.example.weathertrackingapp.domain.useCase

import android.util.Log
import com.example.weathertrackingapp.common.appState.ResultState
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.weatherException.CustomException
import com.example.weathertrackingapp.domain.model.CurrentConditions
import com.example.weathertrackingapp.domain.model.WeatherRequest
import com.example.weathertrackingapp.domain.repository.WeatherRepository

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
) {

    operator fun invoke(weatherRequest: WeatherRequest): ResultState<CurrentConditions> {
        Log.d(TAG, "invoke: getting current weather for $weatherRequest")
        return try {
            val currentConditions = weatherRepository.getCurrentWeather(weatherRequest)
            ResultState.Success(currentConditions)
        } catch (e: CustomException) {
            Log.e(TAG, "invoke: error getting current weather", e)
            ResultState.Failure(e)
        }
    }

}