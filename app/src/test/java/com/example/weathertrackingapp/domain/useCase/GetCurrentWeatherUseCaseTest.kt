package com.example.weathertrackingapp.domain.useCase

import com.example.weathertrackingapp.domain.customState.DomainState
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.fake.MockedRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetCurrentWeatherUseCaseTest() {
    private lateinit var getCurrentWeather: GetCurrentWeatherUseCase
    private lateinit var weatherRepository: MockedRepository
    private lateinit var subscriber: TestSubscriber<CurrentWeatherEntity>
    private lateinit var weatherRequest: WeatherRequest

    @BeforeEach
    fun setup() {
        weatherRepository = MockedRepository()
        getCurrentWeather = GetCurrentWeatherUseCase(weatherRepository)
        subscriber = TestSubscriber()
        getCurrentWeather.registerSubscriber(subscriber)
        weatherRequest = WeatherRequest(LatLong(latitude = 31.0, longitude = 30.0), "ar")
        getCurrentWeather.registerSubscriber(subscriber)
    }
    @Test
    fun assertOk(){
        assertEquals(1,1)
    }

    @Test
    fun `Given weather request, When invoking the useCase, Then subscriper get loading`() {
        //Given

        //When
        getCurrentWeather(weatherRequest)
        //Then
        val firstUpdate = subscriber.updates[0]
        val secondUpdate = subscriber.updates[1]
        val lastUpdate = subscriber.updates.last()

        assertEquals(expected = DomainState.Loading(true), actual = firstUpdate)
        assertEquals(
            expected = DomainState.SuccessWithFreshData(
                CurrentWeatherEntity()
            ), actual = secondUpdate
        )
        assertEquals(DomainState.Loading(false), subscriber.updates[2])

    }


}