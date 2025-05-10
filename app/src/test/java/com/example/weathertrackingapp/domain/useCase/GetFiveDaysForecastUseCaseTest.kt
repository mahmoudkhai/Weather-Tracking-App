package com.example.weathertrackingapp.domain.useCase

import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.fake.MockedRepository
import com.example.weathertrackingapp.domain.fake.MockedRepository.Companion.shouldFail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFiveDaysForecastUseCaseTest {

    private lateinit var getFiveDaysWeather: GetFiveDaysForecastUseCase
    private lateinit var weatherRepository: MockedRepository
    private lateinit var subscriber: TestSubscriber<FiveDaysForecastEntity>
    private lateinit var weatherRequest: WeatherRequest
    private lateinit var firstUpdate: DomainState<FiveDaysForecastEntity>
    private lateinit var lastUpdate: DomainState<FiveDaysForecastEntity>

    @BeforeEach
    fun setUp() {
        weatherRepository = MockedRepository()
        subscriber = TestSubscriber()
        getFiveDaysWeather = GetFiveDaysForecastUseCase(weatherRepository)
        getFiveDaysWeather.registerSubscriber(subscriber)
        weatherRequest = WeatherRequest(LatLong(latitude = 31.0, longitude = 30.0), "ar")
    }

    @Test
    fun `Given weather request, When invoking the useCase, Then subscriber get loading then success with data, then stop loading`() {
        //Given
        weatherRepository.apply {
            shouldFail = false
        }
        //When
        getFiveDaysWeather(weatherRequest)
        firstUpdate = subscriber.updates[FIRST_STATE]
        val secondUpdate = subscriber.updates[SECOND_STATE]
        lastUpdate = subscriber.updates[LAST_STATE]
        //Then
        assertEquals(expected = DomainState.Loading(true), actual = firstUpdate)
        assertEquals(
            expected = DomainState.SuccessWithFreshData(FiveDaysForecastEntity()),
            actual = secondUpdate
        )
        assertEquals(DomainState.Loading(false), lastUpdate)
    }

    @Test
    fun `Given weather request, When invoking the useCae and api request fails & no cached data found, Then secondUpdate is failure with no cached data`() {
        //Given
        val exceptions = listOf(
            CustomException.NetworkException.NoInternetConnection,
            CustomException.DataException.NoCachedDataFound
        )
        weatherRepository.apply {
            shouldFail = true
            MockedRepository.exceptions = exceptions
            MockedRepository.fiveDaysForecastCachedData = null
        }
        //When
        getFiveDaysWeather(weatherRequest)
        firstUpdate = subscriber.updates[FIRST_STATE]
        val secondUpdate = subscriber.updates[SECOND_STATE]
        lastUpdate = subscriber.updates[LAST_STATE]
        //Then
        assertEquals(expected = DomainState.Loading(true), actual = firstUpdate)
        assertEquals(
            expected = DomainState.FailureWithCachedData(exceptions), actual = secondUpdate
        )
        assertEquals(DomainState.Loading(false), lastUpdate)
    }

    @Test
    fun `Given weather request, When invoking the useCae and api request fails but cached data found, Then secondUpdate is failure with cached data`() {
        //Given
        val exceptions = listOf(CustomException.NetworkException.NoInternetConnection)
        val cachedData = FiveDaysForecastEntity()
        weatherRepository.apply {
            shouldFail = true
            MockedRepository.exceptions = exceptions
            MockedRepository.fiveDaysForecastCachedData = cachedData
        }
        //When
        getFiveDaysWeather(weatherRequest)
        firstUpdate = subscriber.updates[FIRST_STATE]
        val secondUpdate = subscriber.updates[SECOND_STATE]
        lastUpdate = subscriber.updates[LAST_STATE]
        //Then
        assertEquals(expected = DomainState.Loading(true), actual = firstUpdate)
        assertEquals(
            expected = DomainState.FailureWithCachedData(exceptions, cachedData),
            actual = secondUpdate
        )
        assertEquals(DomainState.Loading(false), lastUpdate)
    }

    companion object {
        const val FIRST_STATE = 0
        const val SECOND_STATE = 1
        const val LAST_STATE = 2
    }

}