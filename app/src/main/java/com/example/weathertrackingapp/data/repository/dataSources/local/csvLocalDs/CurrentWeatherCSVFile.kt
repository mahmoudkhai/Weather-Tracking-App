package com.example.weathertrackingapp.data.repository.dataSources.local.csvLocalDs

import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
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

    override fun fromDtoToCsvRow(entity: CurrentWeatherDto): String {
        val rowString = StringBuilder().apply {
            append(entity.cloudcover)
            append("$CSV_SEPARATOR${entity.conditions}")
            append("$CSV_SEPARATOR${entity.datetime}")
            append("$CSV_SEPARATOR${entity.datetimeEpoch}")
            append("$CSV_SEPARATOR${entity.dew}")
            append("$CSV_SEPARATOR${entity.feelslike}")
            append("$CSV_SEPARATOR${entity.humidity}")
            append("$CSV_SEPARATOR${entity.icon}")
            append("$CSV_SEPARATOR${entity.moonphase}")
            append("$CSV_SEPARATOR${entity.precipprob}")
            append("$CSV_SEPARATOR${entity.pressure}")
            append("$CSV_SEPARATOR${entity.snow}")
            append("$CSV_SEPARATOR${entity.snowdepth}")
            append("$CSV_SEPARATOR${entity.solarenergy}")
            append("$CSV_SEPARATOR${entity.solarradiation}")
            append("$CSV_SEPARATOR${entity.source}")
            append("$CSV_SEPARATOR${entity.stations.joinToString(STATIONS_SEPARATOR)}")
            append("$CSV_SEPARATOR${entity.sunrise}")
            append("$CSV_SEPARATOR${entity.sunriseEpoch}")
            append("$CSV_SEPARATOR${entity.sunset}")
            append("$CSV_SEPARATOR${entity.sunsetEpoch}")
            append("$CSV_SEPARATOR${entity.temp}")
            append("$CSV_SEPARATOR${entity.uvindex}")
            append("$CSV_SEPARATOR${entity.visibility}")
            append("$CSV_SEPARATOR${entity.winddir}")
            append("$CSV_SEPARATOR${entity.windspeed}")
        }
        return rowString.toString()
    }

    override fun fromCsvRowToDto(row: String): CurrentWeatherDto {
        val data = row.split(CSV_SEPARATOR, limit = SPLIT_LIMIT)

        return CurrentWeatherDto(
            cloudcover = data[CLOUDCOVER].toDouble(),
            conditions = data[CONDITIONS],
            datetime = data[DATETIME],
            datetimeEpoch = data[DATETIME_EPOCH].toInt(),
            dew = data[DEW].toDouble(),
            feelslike = data[FEELSLIKE].toDouble(),
            humidity = data[HUMIDITY].toDouble(),
            icon = data[ICON],
            moonphase = data[MOONPHASE].toDouble(),
            precipprob = data[PRECIPPROB].toDouble(),
            pressure = data[PRESSURE].toDouble(),
            snow = data[SNOW].toDouble(),
            snowdepth = data[SNOWDEPTH].toDouble(),
            solarenergy = data[SOLARENERGY].toDouble(),
            solarradiation = data[SOLARRADIATION].toDouble(),
            source = data[SOURCE],
            stations = data[STATIONS].split(STATIONS_SEPARATOR),
            sunrise = data[SUNRISE],
            sunriseEpoch = data[SUNRISE_EPOCH].toInt(),
            sunset = data[SUNSET],
            sunsetEpoch = data[SUNSET_EPOCH].toInt(),
            temp = data[TEMP].toDouble(),
            uvindex = data[UVINDEX].toDouble(),
            visibility = data[VISIBILITY].toDouble(),
            winddir = data[WINDDIR].toDouble(),
            windspeed = data[WINDSPEED].toDouble()
        )
    }

    companion object CurrentWeatherIndices {
        const val CSV_SEPARATOR = ","
        const val STATIONS_SEPARATOR = "|"
        const val SPLIT_LIMIT = 26

        const val CLOUDCOVER = 0
        const val CONDITIONS = 1
        const val DATETIME = 2
        const val DATETIME_EPOCH = 3
        const val DEW = 4
        const val FEELSLIKE = 5
        const val HUMIDITY = 6
        const val ICON = 7
        const val MOONPHASE = 8
        const val PRECIPPROB = 9
        const val PRESSURE = 10
        const val SNOW = 11
        const val SNOWDEPTH = 12
        const val SOLARENERGY = 13
        const val SOLARRADIATION = 14
        const val SOURCE = 15
        const val STATIONS = 16
        const val SUNRISE = 17
        const val SUNRISE_EPOCH = 18
        const val SUNSET = 19
        const val SUNSET_EPOCH = 20
        const val TEMP = 21
        const val UVINDEX = 22
        const val VISIBILITY = 23
        const val WINDDIR = 24
        const val WINDSPEED = 25

        val HEADERS = listOf(
            "cloudcover", "conditions", "datetime", "datetimeEpoch", "dew",
            "feelslike", "humidity", "icon", "moonphase", "precipprob",
            "pressure", "snow", "snowdepth", "solarenergy", "solarradiation",
            "source", "stations", "sunrise", "sunriseEpoch", "sunset",
            "sunsetEpoch", "temp", "uvindex", "visibility", "winddir", "windspeed"
        )
    }
}
