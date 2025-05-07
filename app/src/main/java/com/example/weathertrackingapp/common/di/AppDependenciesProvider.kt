package com.example.weathertrackingapp.common.di

import com.example.weathertrackingapp.data.dataSources.remote.WeatherRemoteDSImpl
import com.example.weathertrackingapp.data.dataSources.remote.apiService.ApiService
import com.example.weathertrackingapp.data.dataSources.remote.apiService.ApiServiceImpl
import com.example.weathertrackingapp.data.repository.WeatherRepositoryImpl
import com.example.weathertrackingapp.domain.repository.WeatherRepository
import com.example.weathertrackingapp.domain.repository.dataSources.remote.WeatherRemoteDS
import com.example.weathertrackingapp.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathertrackingapp.domain.useCase.GetFiveDaysForecastUseCase
import com.example.weathertrackingapp.presentation.fragments.currentWeather.CurrentWeatherViewModel
import com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase.FiveDaysForecastViewModel

object AppDependenciesProvider {

    fun provideCurrentWeatherViewModel(): CurrentWeatherViewModel {
        return CurrentWeatherViewModel(provideGetCurrentWeatherUseCase())
    }

    fun provideFiveDaysForecastViewModel(): FiveDaysForecastViewModel {
        return FiveDaysForecastViewModel(provideGetFiveDaysForecastUseCase())
    }

    private fun provideGetCurrentWeatherUseCase(): GetCurrentWeatherUseCase {
        return GetCurrentWeatherUseCase(
            provideWeatherRepository(
                provideWeatherRemoteDS(
                    provideApiService()
                )
            )
        )
    }

    private fun provideGetFiveDaysForecastUseCase(): GetFiveDaysForecastUseCase {
        return GetFiveDaysForecastUseCase(
            provideWeatherRepository(
                provideWeatherRemoteDS(
                    provideApiService()
                )
            )
        )
    }

    private fun provideApiService(): ApiService = ApiServiceImpl()

    private fun provideWeatherRemoteDS(apiService: ApiService): WeatherRemoteDS =
        WeatherRemoteDSImpl(apiService)

    private fun provideWeatherRepository(remoteDS: WeatherRemoteDS): WeatherRepository =
        WeatherRepositoryImpl(remoteDS)
}