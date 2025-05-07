package com.example.weathertrackingapp.data.mappers

import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeather

object CurrentConditionsMapper : Mapper<CurrentConditionsDto, CurrentWeather> {
    override fun dtoToDomain(input: CurrentConditionsDto): CurrentWeather {
        return CurrentWeather(
            temperature = input.temp,
            feelsLike = input.feelslike,
            conditions = input.conditions,
            icon = input.icon,
            humidity = input.humidity,
            cloudCover = input.cloudcover,
            windSpeed = input.windspeed,
            uvIndex = input.uvindex,
            sunrise = input.sunrise,
            sunset = input.sunset
        )
    }
}