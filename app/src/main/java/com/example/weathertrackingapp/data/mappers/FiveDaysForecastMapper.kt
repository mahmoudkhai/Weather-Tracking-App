package com.example.weathertrackingapp.data.mappers

import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity

object FiveDaysForecastMapper :
    DataToDomainMapper<FiveDaysForecastDto, FiveDaysForecastEntity> {


    override fun dtoToEntity(input: FiveDaysForecastDto): FiveDaysForecastEntity {
        return FiveDaysForecastEntity(
            resolvedAddress = input.resolvedAddress,
            address = input.address,
            timezone = input.timezone,
            days = input.days?.map { WholeDayWeatherMapper.dtoToEntity(it) }
        )
    }


}