package com.example.weathertrackingapp.domain.useCase

import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.fake.MockedRepository
import com.example.weathertrackingapp.domain.fake.MockedRepository.Companion.exceptions
import com.example.weathertrackingapp.domain.fake.MockedRepository.Companion.shouldFail
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetCurrentWeatherUseCaseTest() {
    private lateinit var getCurrentWeather: GetCurrentWeatherUseCase
    private lateinit var weatherRepository: MockedRepository
    private lateinit var subscriber: TestSubscriber<CurrentWeatherEntity>
    private lateinit var weatherRequest: WeatherRequest
    private lateinit var firstUpdate: DomainState<CurrentWeatherEntity>
    private lateinit var lastUpdate: DomainState<CurrentWeatherEntity>

    @BeforeEach
    fun setUp() {
        weatherRepository = MockedRepository()
        subscriber = TestSubscriber()
        getCurrentWeather = GetCurrentWeatherUseCase(weatherRepository)
        getCurrentWeather.registerSubscriber(subscriber)
        weatherRequest = WeatherRequest(LatLong(latitude = 31.0, longitude = 30.0), "ar")
    }

    @Test
    fun `Given weather request, When invoking the useCase, Then subscriper get loading then success with data, then stop loading`() {
        //Given
        weatherRepository.apply {
            shouldFail = false
        }
        //When
        getCurrentWeather(weatherRequest)
        firstUpdate = subscriber.updates[GetFiveDaysForecastUseCaseTest.FIRST_STATE]
        val secondUpdate = subscriber.updates[GetFiveDaysForecastUseCaseTest.SECOND_STATE]
        lastUpdate = subscriber.updates[GetFiveDaysForecastUseCaseTest.LAST_STATE]
        //Then
        assertEquals(expected = DomainState.Loading(true), actual = firstUpdate)
        assertEquals(
            expected = DomainState.SuccessWithFreshData(CurrentWeatherEntity()),
            actual = secondUpdate
        )
        Assertions.assertEquals(DomainState.Loading(false), lastUpdate)
    }

    @Test
    fun `Given weather request, When invoking the useCae and api request fails & no cached data found, Then secondUpdate is failure with no cached data`() {
        //Given
        val customExceptions = listOf(
            CustomException.NetworkException.NoInternetConnection,
            CustomException.DataException.NoCachedDataFound
        )
        weatherRepository.apply {
            shouldFail = true
            exceptions = customExceptions
            MockedRepository.currentWeatherCachedData = null
        }
        //When
        getCurrentWeather(weatherRequest)
        firstUpdate = subscriber.updates[GetFiveDaysForecastUseCaseTest.FIRST_STATE]
        val secondUpdate = subscriber.updates[GetFiveDaysForecastUseCaseTest.SECOND_STATE]
        lastUpdate = subscriber.updates[GetFiveDaysForecastUseCaseTest.LAST_STATE]
        //Then
        assertEquals(expected = DomainState.Loading(true), actual = firstUpdate)
        assertEquals(
            expected = DomainState.FailureWithCachedData(exceptions), actual = secondUpdate
        )
        Assertions.assertEquals(DomainState.Loading(false), lastUpdate)
    }

    @Test
    fun `Given weather request, When invoking the useCae and api request fails but cached data found, Then secondUpdate is failure with cached data`() {
        //Given
        val customExceptions = listOf(CustomException.NetworkException.NoInternetConnection)
        val cachedData = CurrentWeatherEntity()
        weatherRepository.apply {
            shouldFail = true
            exceptions = customExceptions
            MockedRepository.currentWeatherCachedData = cachedData
        }
        //When
        getCurrentWeather(weatherRequest)
        firstUpdate = subscriber.updates[GetFiveDaysForecastUseCaseTest.FIRST_STATE]
        val secondUpdate = subscriber.updates[GetFiveDaysForecastUseCaseTest.SECOND_STATE]
        lastUpdate = subscriber.updates[GetFiveDaysForecastUseCaseTest.LAST_STATE]
        //Then
        assertEquals(expected = DomainState.Loading(true), actual = firstUpdate)
        assertEquals(
            expected = DomainState.FailureWithCachedData(customExceptions, cachedData),
            actual = secondUpdate
        )
        Assertions.assertEquals(DomainState.Loading(false), lastUpdate)
    }

    companion object {
        const val FIRST_STATE = 0
        const val SECOND_STATE = 1
        const val LAST_STATE = 2
    }


}