package com.example.weathertrackingapp.data.mappers

import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecast

object FiveDaysForecastMapper : Mapper<FiveDaysForecastDto, FiveDaysForecast> {

    override fun dtoToDomain(input: FiveDaysForecastDto): FiveDaysForecast {
        return FiveDaysForecast(
            resolvedAddress = input.resolvedAddress,
            address = input.address,
            timezone = input.timezone,
            days = input.days.map { DayForecastMapper.dtoToDomain(it) }
        )
    }
}