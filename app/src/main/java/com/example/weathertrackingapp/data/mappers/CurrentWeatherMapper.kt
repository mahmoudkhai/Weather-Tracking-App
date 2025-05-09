package com.example.weathertrackingapp.data.mappers

import com.example.weathertrackingapp.data.dto.CurrentConditionDto
import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
import com.example.weathertrackingapp.data.dto.WholeDayWeatherDto
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.presentation.model.WholeDayWeather

object CurrentWeatherMapper : DataToDomainMapper<CurrentWeatherDto, CurrentWeatherEntity> {
    override fun dtoToEntity(input: CurrentWeatherDto): CurrentWeatherEntity {
        return CurrentWeatherEntity(
            resolvedAddress = input.resolvedAddress,
            address = input.address,
            queryCost = input.queryCost,
            timeZone = input.timeZone,
            currentConditions = CurrentConditionMapper.dtoToEntity(
                input.currentConditions ?: CurrentConditionDto()
            ),
            wholeDayWeather = WholeDayWeatherMapper.dtoToEntity(input.days ?: WholeDayWeatherDto())
        )
    }

}