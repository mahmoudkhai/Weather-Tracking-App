package com.example.weathertrackingapp.data.mappers

import com.example.weathertrackingapp.data.dto.WholeDayWeatherDto
import com.example.weathertrackingapp.domain.entity.responseEntities.WholeDayWeatherEntity

object WholeDayWeatherMapper :
    DataToDomainMapper<WholeDayWeatherDto, WholeDayWeatherEntity> {

    override fun dtoToEntity(dto: WholeDayWeatherDto): WholeDayWeatherEntity {
        return WholeDayWeatherEntity(
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