package com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.data.dto.CurrentConditionDto
import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
import com.example.weathertrackingapp.data.dto.WholeDayWeatherDto
import com.example.weathertrackingapp.presentation.model.CurrentConditions
import com.example.weathertrackingapp.presentation.model.WholeDayWeather
import java.io.File
import java.io.FileWriter

class CurrentWeatherCSVFile(
    private val filePath: String,
) : CsvFileHelperImpl<CurrentWeatherDto>(
    filePath = filePath,
    headers = HEADERS
) {
    private val file: File by lazy { File(filePath) }

    init {
        if (!file.exists()) {
            FileWriter(file, true).use { writer ->
                writer.appendLine(HEADERS.joinToString(CSV_SEPARATOR))
            }
        }
    }

    override fun fromDtoToCsvRow(dto: CurrentWeatherDto): String {
        return StringBuilder().apply {
            append("${dto.queryCost}|")
            append("${dto.resolvedAddress}|")
            append("${dto.timeZone}|")
            append("${dto.address}|")
            append("${dto.currentConditions?.pressure}|")
            append("${dto.currentConditions?.datetime}|")
            append("${dto.currentConditions?.feelslike}|")
            append("${dto.currentConditions?.temp}|")
            append("${dto.currentConditions?.conditions}|")
            append("${dto.currentConditions?.icon}|")
            append("${dto.currentConditions?.cloudcover}|")
            append("${dto.currentConditions?.humidity}|")
            append("${dto.currentConditions?.uvindex}|")
            append("${dto.currentConditions?.windspeed}|")
            append("${dto.currentConditions?.sunrise}|")
            append("${dto.currentConditions?.sunset}|")
            append("${dto.days?.description}|")
            append("${dto.days?.conditions}|")
            append("${dto.days?.feelslike}|")
            append("${dto.days?.datetime}|")
            append("${dto.days?.humidity}|")
            append("${dto.days?.precip}|")
            append("${dto.days?.icon}|")
            append("${dto.days?.sunrise}|")
            append("${dto.days?.pressure}|")
            append("${dto.days?.temp}|")
            append("${dto.days?.windspeed}")
        }.toString()
    }


    override fun fromCsvRowToDto(row: String): CurrentWeatherDto {
        // Split the CSV row by commas
        val values = row.split("|")
        Log.d(TAG, "fromCsvRowToDto: $values")

        // Mapping values to respective fields
        val currentConditions = CurrentConditionDto(
            pressure = values[PRESSURE].toDoubleOrNull(),
            datetime = values[DATETIME],
            feelslike = values[FEELSLIKE].toDoubleOrNull(),
            temp = values[TEMP].toDoubleOrNull(),
            conditions = values[CONDITIONS],
            icon = values[ICON],
            cloudcover = values[CLOUDCOVER].toDoubleOrNull(),
            humidity = values[HUMIDITY].toDoubleOrNull(),
            uvindex = values[UVINDEX].toDoubleOrNull(),
            windspeed = values[WINDSPEED].toDoubleOrNull(),
            sunrise = values[SUNRISE],
            sunset = values[SUNSET]
        )

        val wholeDayWeather = WholeDayWeatherDto(
            description = values[DESCRIPTION],
            conditions = values[DAY_CONDITIONS],
            feelslike = values[DAY_FEELSLIKE].toDoubleOrNull(),
            datetime = values[DAY_DATETIME],
            humidity = values[DAY_HUMIDITY].toDoubleOrNull(),
            precip = values[DAY_PRECIP].toDoubleOrNull(),
            icon = values[DAY_ICON],
            sunrise = values[DAY_SUNRISE],
            pressure = values[DAY_PRESSURE].toDoubleOrNull(),
            temp = values[DAY_TEMP].toDoubleOrNull(),
            windspeed = values[DAY_WINDSPEED].toDoubleOrNull()
        )

        return CurrentWeatherDto(
            queryCost = values[QUERY_COST].toIntOrNull(),
            resolvedAddress = values[RESOLVED_ADDRESS],
            timeZone = values[TIME_ZONE],
            address = values[ADDRESS],
            currentConditions = currentConditions,
            days = wholeDayWeather
        ).also {
            Log.d(TAG, "Current Weather Cached Data = ${it.toString()}")
        }

    }


    companion object CurrentWeatherIndices {
        const val CSV_SEPARATOR = ","

        const val QUERY_COST = 0
        const val RESOLVED_ADDRESS = 1
        const val TIME_ZONE = 2
        const val ADDRESS = 4
        const val PRESSURE = 5
        const val DATETIME = 6
        const val FEELSLIKE = 7
        const val TEMP = 8
        const val CONDITIONS = 9
        const val ICON = 10
        const val CLOUDCOVER = 11
        const val HUMIDITY = 12
        const val UVINDEX = 13
        const val WINDSPEED = 14
        const val SUNRISE = 15
        const val SUNSET = 16
        const val DESCRIPTION = 17
        const val DAY_CONDITIONS = 18
        const val DAY_FEELSLIKE = 19
        const val DAY_DATETIME = 20
        const val DAY_HUMIDITY = 21
        const val DAY_PRECIP = 22
        const val DAY_ICON = 23
        const val DAY_SUNRISE = 24
        const val DAY_PRESSURE = 25
        const val DAY_TEMP = 26
        const val DAY_WINDSPEED = 27
        val HEADERS = listOf(
            "cloudcover", "conditions", "datetime", "datetimeEpoch", "dew",
            "feelslike", "humidity", "icon", "moonphase", "precipprob",
            "pressure", "snow", "snowdepth", "solarenergy", "solarradiation",
            "source", "stations", "sunrise", "sunriseEpoch", "sunset",
            "sunsetEpoch", "temp", "uvindex", "visibility", "winddir", "windspeed"
        )
    }
}
