package com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs

import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.data.dto.WholeDayWeatherDto
import java.io.File
import java.io.FileWriter

class FiveDaysForecastCSVFile(
    private val filePath: String,
) : CsvFileHelperImpl<FiveDaysForecastDto>(
    filePath = filePath,
    headers = HEADERS,
) {
    private val file: File by lazy { File(filePath) }

    init {
        if (!file.exists()) {
            FileWriter(file, true).use { writer ->
                writer.appendLine(HEADERS.joinToString(CSV_SEPARATOR))
            }
        }
    }

    override fun fromDtoToCsvRow(entity: FiveDaysForecastDto): String {
        val rowString = StringBuilder().apply {
            append(entity.resolvedAddress)
            append("$CSV_SEPARATOR${entity.address}")
            append("$CSV_SEPARATOR${entity.timezone}")

            val daysString = entity.days?.joinToString(DAY_SEPARATOR) { day ->
                listOf(
                    day.cloudcover, day.conditions, day.datetime, day.datetimeEpoch,
                    day.description, day.dew, day.feelslike, day.feelslikemax,
                    day.feelslikemin, day.humidity, day.icon, day.moonphase,
                    day.precip, day.precipcover, day.precipprob, day.pressure,
                    day.severerisk, day.snow, day.snowdepth, day.solarenergy,
                    day.solarradiation, day.source, day.sunrise, day.sunriseEpoch,
                    day.sunset, day.sunsetEpoch, day.temp, day.tempmax, day.tempmin,
                    day.uvindex, day.visibility, day.winddir, day.windgust, day.windspeed
                ).joinToString(FIELD_SEPARATOR)
            } ?: ""
            append("$CSV_SEPARATOR$daysString")
        }
        return rowString.toString()
    }

    override fun fromCsvRowToDto(row: String): FiveDaysForecastDto {
        val data = row.split(CSV_SEPARATOR, limit = SPLIT_LIMIT)

        val resolvedAddress = data[RESOLVED_ADDRESS]
        val address = data[ADDRESS]
        val timezone = data[TIMEZONE]
        val daysString = data[DAYS]

        val days: List<WholeDayWeatherDto> = daysString.split(DAY_SEPARATOR).map { dayStr ->
            val parts = dayStr.split(FIELD_SEPARATOR)
            WholeDayWeatherDto(
                cloudcover = parts[CLOUDCOVER].toDouble(),
                conditions = parts[CONDITIONS],
                datetime = parts[DATETIME],
                datetimeEpoch = parts[DATETIME_EPOCH].toInt(),
                description = parts[DESCRIPTION],
                dew = parts[DEW].toDouble(),
                feelslike = parts[FEELSLIKE].toDouble(),
                feelslikemax = parts[FEELSLIKEMAX].toDouble(),
                feelslikemin = parts[FEELSLIKEMIN].toDouble(),
                humidity = parts[HUMIDITY].toDouble(),
                icon = parts[ICON],
                moonphase = parts[MOONPHASE].toDouble(),
                precip = parts[PRECIP].toDouble(),
                precipcover = parts[PRECIPCOVER].toDouble(),
                precipprob = parts[PRECIPPROB].toDouble(),
                pressure = parts[PRESSURE].toDouble(),
                severerisk = parts[SEVERERISK].toDouble(),
                snow = parts[SNOW].toDouble(),
                snowdepth = parts[SNOWDEPTH].toDouble(),
                solarenergy = parts[SOLARENERGY].toDouble(),
                solarradiation = parts[SOLARRADIATION].toDouble(),
                source = parts[SOURCE],
                sunrise = parts[SUNRISE],
                sunriseEpoch = parts[SUNRISE_EPOCH].toInt(),
                sunset = parts[SUNSET],
                sunsetEpoch = parts[SUNSET_EPOCH].toInt(),
                temp = parts[TEMP].toDouble(),
                tempmax = parts[TEMPMAX].toDouble(),
                tempmin = parts[TEMPMIN].toDouble(),
                uvindex = parts[UVINDEX].toDouble(),
                visibility = parts[VISIBILITY].toDouble(),
                winddir = parts[WINDDIR].toDouble(),
                windgust = parts[WINDGUST].toDouble(),
                windspeed = parts[WINDSPEED].toDouble()
            )
        }

        return FiveDaysForecastDto(
            resolvedAddress = resolvedAddress,
            address = address,
            timezone = timezone,
            days = days
        )
    }


    companion object FiveDaysForecastIndicies {
        const val CSV_SEPARATOR = ","
        const val DAY_SEPARATOR = "##"
        const val FIELD_SEPARATOR = "|"
        const val DAYS = 3
        const val SPLIT_LIMIT = DAYS + 2
        const val RESOLVED_ADDRESS = 0
        const val ADDRESS = 1
        const val TIMEZONE = 2
        const val CLOUDCOVER = 0
        const val CONDITIONS = 1
        const val DATETIME = 2
        const val DATETIME_EPOCH = 3
        const val DESCRIPTION = 4
        const val DEW = 5
        const val FEELSLIKE = 6
        const val FEELSLIKEMAX = 7
        const val FEELSLIKEMIN = 8
        const val HUMIDITY = 9
        const val ICON = 10
        const val MOONPHASE = 11
        const val PRECIP = 12
        const val PRECIPCOVER = 13
        const val PRECIPPROB = 14
        const val PRESSURE = 15
        const val SEVERERISK = 16
        const val SNOW = 17
        const val SNOWDEPTH = 18
        const val SOLARENERGY = 19
        const val SOLARRADIATION = 20
        const val SOURCE = 21
        const val SUNRISE = 22
        const val SUNRISE_EPOCH = 23
        const val SUNSET = 24
        const val SUNSET_EPOCH = 25
        const val TEMP = 26
        const val TEMPMAX = 27
        const val TEMPMIN = 28
        const val UVINDEX = 29
        const val VISIBILITY = 30
        const val WINDDIR = 31
        const val WINDGUST = 32
        const val WINDSPEED = 33
        val HEADERS = listOf(
            "resolvedAddress",
            "address",
            "timezone",
            "days",
        )
    }


}