package com.example.weathertrackingapp.data.mappers

import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.model.responseModels.CurrentConditions

object CurrentConditionsMapper : Mapper<CurrentConditionsDto, CurrentConditions> {
    override fun dtoToDomain(input: CurrentConditionsDto): CurrentConditions {
        return CurrentConditions(
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