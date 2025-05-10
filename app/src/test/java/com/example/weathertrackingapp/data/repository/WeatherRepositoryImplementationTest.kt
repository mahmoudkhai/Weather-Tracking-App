package com.example.weathertrackingapp.data.repository

import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.data.fake.MockedLocalDs
import com.example.weathertrackingapp.data.fake.MockedRemoteDS
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentConditionsEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.repository.WeatherRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WeatherRepositoryImplementationTest {
    private lateinit var weatherRemoteDS: MockedRemoteDS
    private lateinit var weatherLocalDS: MockedLocalDs
    private lateinit var weatherRepository: WeatherRepository

    @BeforeEach
    fun setup() {
        weatherRemoteDS = MockedRemoteDS()
        weatherLocalDS = MockedLocalDs()
        weatherRepository = WeatherRepositoryImpl(weatherRemoteDS, weatherLocalDS)
    }

    @Test
    fun `Give weather request, When calling api to get current weather success, Then should return data`() {
        //Given
        val weatherRequest = getWeatherRequest()
        //When
        weatherRemoteDS.remoteCallShouldFail = false
        val result = weatherRepository.getCurrentWeather(weatherRequest)
        //Then
        assertEquals(
            expected = DomainState.SuccessWithFreshData(
                CurrentWeatherEntity(currentConditions = CurrentConditionsEntity()),
            ), actual = result
        )
    }

    @Test
    fun `Give weather request, When calling api fails to get current weather and database call fails, Then should return failure`() {
        //Given
        val weatherRequest = getWeatherRequest()
        //When
        weatherRemoteDS.remoteCallShouldFail = true
        weatherLocalDS.localCallShouldFail = true
        val result = weatherRepository.getCurrentWeather(weatherRequest)
        //Then
        assertTrue((result as DomainState.FailureWithCachedData).cachedData == null)
        assertIs<List<CustomException>>((result as? DomainState.FailureWithCachedData)?.exception)
    }

    @Test
    fun `Give weather request, When calling api to get current weather fails, Then should call the local database and get data`() {
        //Given
        val weatherRequest = getWeatherRequest()
        //When
        weatherRemoteDS.remoteCallShouldFail = true
        weatherLocalDS.localCallShouldFail = false
        val result: DomainState<CurrentWeatherEntity> =
            weatherRepository.getCurrentWeather(weatherRequest)
        //Then
        assertIs<List<CustomException>>((result as? DomainState.FailureWithCachedData)?.exception)
        assertNotNull((result as DomainState.FailureWithCachedData).cachedData)
    }

    @Test
    fun `Give weather request, When calling api to get five days forecast success, Then should return data`() {
        //Given
        val weatherRequest = getWeatherRequest()
        //When
        weatherRemoteDS.remoteCallShouldFail = false
        val result = weatherRepository.getFiveDaysForecast(weatherRequest)
        //Then
        assertIs<DomainState<FiveDaysForecastEntity>>(result)
    }

    @Test
    fun `Give weather request, When calling api to get five days forecast fails and database call fails, Then should return failure`() {
        //Given
        val weatherRequest = getWeatherRequest()
        //When
        weatherRemoteDS.remoteCallShouldFail = true
        weatherLocalDS.localCallShouldFail = true
        val result = weatherRepository.getFiveDaysForecast(weatherRequest)
        //Then
        assertTrue((result as DomainState.FailureWithCachedData).cachedData == null)
        assertIs<List<CustomException>>((result as? DomainState.FailureWithCachedData)?.exception)
    }

    @Test
    fun `Give weather request, When calling api to get five days forecast fails, Then should call the local database and get data`() {
        //Given
        val weatherRequest = getWeatherRequest()
        //When
        weatherRemoteDS.remoteCallShouldFail = true
        weatherLocalDS.localCallShouldFail = false
        val result: DomainState<FiveDaysForecastEntity> =
            weatherRepository.getFiveDaysForecast(weatherRequest)
        //Then
        assertIs<List<CustomException>>((result as? DomainState.FailureWithCachedData)?.exception)
        assertNotNull((result as DomainState.FailureWithCachedData).cachedData)
    }


    private fun getWeatherRequest() = WeatherRequest(LatLong(31.0, 30.1))

}