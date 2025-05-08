package com.example.weathertrackingapp.common.di

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.data.repository.WeatherRepositoryImpl
import com.example.weathertrackingapp.data.repository.dataSources.local.WeatherLocalDSImpl
import com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs.CurrentWeatherCSVFile
import com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs.FiveDaysForecastCSVFile
import com.example.weathertrackingapp.data.repository.dataSources.remote.WeatherRemoteDSImpl
import com.example.weathertrackingapp.data.repository.dataSources.remote.apiService.ApiService
import com.example.weathertrackingapp.data.repository.dataSources.remote.apiService.ApiServiceImpl
import com.example.weathertrackingapp.domain.repository.WeatherRepository
import com.example.weathertrackingapp.domain.repository.dataSources.local.WeatherLocalDS
import com.example.weathertrackingapp.domain.repository.dataSources.remote.WeatherRemoteDS
import com.example.weathertrackingapp.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathertrackingapp.domain.useCase.GetFiveDaysForecastUseCase
import com.example.weathertrackingapp.presentation.WeatherApp
import com.example.weathertrackingapp.presentation.WeatherApp.Companion.getAppContext
import com.example.weathertrackingapp.presentation.fragments.currentWeather.CurrentWeatherViewModel
import com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase.FiveDaysForecastViewModel
import java.io.File

//by lazy will for singletons
object AppDependenciesProvider {

    private val apiService: ApiService by lazy {
        ApiServiceImpl()
    }

    private val weatherRemoteDS: WeatherRemoteDS by lazy {
        WeatherRemoteDSImpl(apiService)
    }

    private val weatherLocalDS: WeatherLocalDS by lazy {
        WeatherLocalDSImpl(
            currentWeatherCSVFile = getCurrentWeatherCSVFileInstance(),
            fiveDaysForecastCSVFile = getFiveDaysForecastCSVFileInstance()
        )
    }

    private val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(weatherRemoteDS, weatherLocalDS)
    }

    private fun provideGetCurrentWeatherUseCase(): GetCurrentWeatherUseCase {
        return GetCurrentWeatherUseCase(weatherRepository)
    }

    private fun provideGetFiveDaysForecastUseCase(): GetFiveDaysForecastUseCase {
        return GetFiveDaysForecastUseCase(weatherRepository)
    }

    fun provideCurrentWeatherViewModel(): CurrentWeatherViewModel {
        return CurrentWeatherViewModel(provideGetCurrentWeatherUseCase())
    }

    fun provideFiveDaysForecastViewModel(): FiveDaysForecastViewModel {
        return FiveDaysForecastViewModel(provideGetFiveDaysForecastUseCase())
    }

    private fun getCurrentWeatherCSVFileInstance() = CurrentWeatherCSVFile(run {
        val baseDir = File(getAppContext().filesDir, "weather app")
        if (!baseDir.exists()) {
            baseDir.mkdirs()
        }
        File(baseDir, "currentWeather.csv").absolutePath.also {
            Log.d(TAG, "current weather csv file created with path = : $it ")
        }
    })

    private fun getFiveDaysForecastCSVFileInstance() = FiveDaysForecastCSVFile(run {
        val baseDir = File(getAppContext().filesDir, "weather app")
        if (!baseDir.exists()) {
            baseDir.mkdirs()
        }
        File(baseDir, "fiveDaysForecast.csv").absolutePath.also {
            Log.d(TAG, "five days weather forecast csv file created with path = : $it ")
        }
    })

}
