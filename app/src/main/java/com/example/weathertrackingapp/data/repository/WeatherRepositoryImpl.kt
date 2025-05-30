package com.example.weathertrackingapp.data.repository

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.data.mappers.CurrentWeatherMapper
import com.example.weathertrackingapp.data.mappers.FiveDaysForecastMapper
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.repository.WeatherRepository
import com.example.weathertrackingapp.domain.repository.dataSources.local.WeatherLocalDS
import com.example.weathertrackingapp.domain.repository.dataSources.remote.WeatherRemoteDS
import org.json.JSONException
import java.io.IOException

class WeatherRepositoryImpl(
    private val weatherRemoteDS: WeatherRemoteDS,
    private val weatherLocalDS: WeatherLocalDS,
) : WeatherRepository {

    override fun getCurrentWeather(weatherRequest: WeatherRequest): DomainState<CurrentWeatherEntity> {
        return safeDataSourceCall<CurrentWeatherEntity>(
            remoteDSCall = {
                val currentWeatherDto = weatherRemoteDS.getCurrentWeather(weatherRequest)
                weatherLocalDS.cacheCurrentWeatherDto(currentWeatherDto)
                CurrentWeatherMapper.dtoToEntity(currentWeatherDto)
            },
            localDSCall = {
                val localData = weatherLocalDS.getCurrentWeatherDto()
                CurrentWeatherMapper.dtoToEntity(localData)
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

    /**
     * Tries to get data safely from the remote data source first.
     *
     * If the remote call succeeds, it wraps the result in a DomainState.SuccessWithFreshData.
     *
     * If the remote call fails it wrap thrown exception with a CustomException, and it falls back to the local data source
     * and returns the result wrapped as DomainState.FailureWithCachedData.
     *
     * If local data source was empty or an exception is thrown it returns a DomainState.FailureWithCachedData with null cached data.
     *
     * This method helps ensure the app always tries the freshest data first (remote), but gracefully
     * handles failures by using local data if available.
     *
     * @return A DomainState object representing either the fresh remote data or the fallback local data.
     */
    private fun <T> safeDataSourceCall(
        remoteDSCall: () -> T,
        localDSCall: () -> T,
    ): DomainState<T> {
        return try {
            DomainState.SuccessWithFreshData(safeRemoteDSCall(remoteDSCall))
        } catch (remoteDSException: CustomException) {
            safeLocalDSCall(remoteDSException, localDSCall)
        }
    }

    private fun <T> safeRemoteDSCall(
        remoteDSCall: () -> T,
    ): T {
        return try {
            remoteDSCall()
        } catch (exception: Exception) {
            throw when (exception) {
                is IOException -> CustomException.NetworkException.NoInternetConnection
                is JSONException -> CustomException.DataException.ParsingException
                is CustomException -> exception
                else -> {
                    Log.d(TAG, "api call exception $exception")
                    CustomException.NetworkException.UnKnownNetworkException("Unknown error with message: ${exception.message}")
                }
            }
        }
    }

    private fun <T> safeLocalDSCall(
        remoteDSException: CustomException,
        localDSCall: () -> T,
    ): DomainState<T> = try {
        DomainState.FailureWithCachedData(
            exception = listOf(remoteDSException),
            cachedData = localDSCall()
        )
    } catch (e: Exception) {
        val localException = when (e) {
            is IOException -> CustomException.DataException.LocalInputOutputException
            is IndexOutOfBoundsException -> {
                Log.d(TAG, "IndexOutOfBoundsException = $e")
                CustomException.DataException.NoCachedDataFound
            }

            else -> CustomException.DataException.UnKnownDataException(e)
        }
        DomainState.FailureWithCachedData(exception = listOf(remoteDSException, localException))
    }


}