package com.example.weathertrackingapp.data.mappers

import com.example.weathertrackingapp.data.dto.CurrentConditionDto
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentConditionsEntity

object CurrentConditionMapper : DataToDomainMapper<CurrentConditionDto, CurrentConditionsEntity> {
    override fun dtoToEntity(input: CurrentConditionDto): CurrentConditionsEntity {
        return CurrentConditionsEntity(
            dateTime = input.datetime,
            pressure = input.pressure,
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