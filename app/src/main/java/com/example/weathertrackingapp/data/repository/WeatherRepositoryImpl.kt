package com.example.weathertrackingapp.data.repository

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.data.mappers.CurrentConditionsMapper
import com.example.weathertrackingapp.data.mappers.FiveDaysForecastMapper
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.repository.WeatherRepository
import com.example.weathertrackingapp.domain.repository.dataSources.local.WeatherLocalDS
import com.example.weathertrackingapp.domain.repository.dataSources.remote.WeatherRemoteDS

class WeatherRepositoryImpl(
    private val weatherRemoteDS: WeatherRemoteDS,
    private val weatherLocalDS: WeatherLocalDS,
) : WeatherRepository {

    override fun getCurrentWeather(weatherRequest: WeatherRequest): DomainState<CurrentWeatherEntity> {
        return safeDataSourceCall<CurrentWeatherEntity>(
            remoteDSCall = {
                val currentWeatherDto = weatherRemoteDS.getCurrentWeather(weatherRequest)
                weatherLocalDS.cacheCurrentWeatherDto(currentWeatherDto)
                CurrentConditionsMapper.dtoToEntity(currentWeatherDto)
            },
            localDSCall = {
                val localData = weatherLocalDS.getCurrentWeatherDto()
                CurrentConditionsMapper.dtoToEntity(localData).also {
                    Log.d(TAG, "getCurrentWeather: from local data source entity = $it")
                }
            }
        )
    }

    override fun getFiveDaysForecast(weatherRequest: WeatherRequest): DomainState<FiveDaysForecastEntity> {
        return safeDataSourceCall<FiveDaysForecastEntity>(
            remoteDSCall = {
                val fiveDaysForecastDto = weatherRemoteDS.getFiveDaysForecast(weatherRequest)
                weatherLocalDS.cacheFiveDaysForecastDto(fiveDaysForecastDto)
                FiveDaysForecastMapper.dtoToEntity(fiveDaysForecastDto)
            },
            localDSCall = {
                val localData = weatherLocalDS.getFiveDaysForecastDto()
                FiveDaysForecastMapper.dtoToEntity(localData)
            }
        )
    }


    private fun <T> safeDataSourceCall(
        remoteDSCall: () -> T,
        localDSCall: () -> T,
    ): DomainState<T> {
        return try {
            DomainState.SuccessWithFreshData(remoteDSCall())
        } catch (remoteDSException: CustomException) {
            Log.e(
                TAG,
                "Error fetching from remote DS source: ${remoteDSException.localizedMessage}"
            )
            try {
                val localData = localDSCall()
                DomainState.FailureWithCachedData(listOf(remoteDSException), localData)
            } catch (localDSException: CustomException) {
                Log.e(
                    TAG,
                    "Error fetching from local DS storage: ${localDSException.localizedMessage}"
                )
                DomainState.FailureWithCachedData(listOf(remoteDSException, localDSException))
            }
        }
    }

}