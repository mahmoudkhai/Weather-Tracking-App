package com.example.weathertrackingapp.data.mappers

import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity

object CurrentConditionsMapper : DataToDomainMapper<CurrentWeatherDto, CurrentWeatherEntity> {
    override fun dtoToEntity(input: CurrentWeatherDto): CurrentWeatherEntity {
        return CurrentWeatherEntity(
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