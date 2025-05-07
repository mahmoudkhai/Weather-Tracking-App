package com.example.weathertrackingapp.data.mappers

import com.example.weathertrackingapp.data.dto.DayForecastDto
import com.example.weathertrackingapp.domain.entity.responseEntities.DayForecast

object DayForecastMapper : Mapper<DayForecastDto, DayForecast> {

    override fun dtoToDomain(dto: DayForecastDto): DayForecast {
        return DayForecast(
            description = dto.description,
            conditions = dto.conditions,
            datetime = dto.datetime,
            feelslike = dto.feelslike,
            humidity = dto.humidity,
            icon = dto.icon,
            precip = dto.precip,
            pressure = dto.pressure,
            sunrise = dto.sunrise,
            temp = dto.temp,
            windspeed = dto.windspeed
        )
    }
}