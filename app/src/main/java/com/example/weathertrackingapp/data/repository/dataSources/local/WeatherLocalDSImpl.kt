package com.example.weathertrackingapp.data.repository.dataSources.local

import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs.CurrentWeatherCSVFile
import com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs.FiveDaysForecastCSVFile
import com.example.weathertrackingapp.domain.repository.dataSources.local.WeatherLocalDS

class WeatherLocalDSImpl(
    private val currentWeatherCSVFile: CurrentWeatherCSVFile,
    private val fiveDaysForecastCSVFile: FiveDaysForecastCSVFile,
) : WeatherLocalDS {
    override fun cacheCurrentWeatherDto(currentWeatherEntity: CurrentWeatherDto) =
        currentWeatherCSVFile.writeDtoAsString(currentWeatherEntity)

    override fun getCurrentWeatherDto(): CurrentWeatherDto =
        currentWeatherCSVFile.readMoseRecentDto()

    override fun cacheFiveDaysForecastDto(fiveDaysForecastDto: FiveDaysForecastDto) =
        fiveDaysForecastCSVFile.writeDtoAsString(fiveDaysForecastDto)

    override fun getFiveDaysForecastDto(): FiveDaysForecastDto =
        fiveDaysForecastCSVFile.readMoseRecentDto()
}