package com.example.weathertrackingapp.presentation.model.extMappers

import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.WholeDayWeatherEntity
import com.example.weathertrackingapp.presentation.model.CurrentWeather
import com.example.weathertrackingapp.presentation.model.FiveDaysForecast
import com.example.weathertrackingapp.presentation.model.WholeDayWeather

// here i used another approach instead of data layer approach that i applied, i know this is inconsistent but i'm learning.
// and i feel this approach is better than creating an Object for mapping because Object will live as long as the app live.
fun CurrentWeatherEntity.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        temperature = this.temperature,
        feelsLike = this.feelsLike,
        conditions = this.conditions,
        icon = this.icon,
        humidity = this.humidity,
        cloudCover = this.cloudCover,
        windSpeed = this.windSpeed,
        uvIndex = this.uvIndex,
        sunrise = this.sunrise,
        sunset = this.sunset
    )
}

fun FiveDaysForecastEntity.toFiveDaysForecast(): FiveDaysForecast {
    return FiveDaysForecast(
        resolvedAddress = this.resolvedAddress,
        address = this.address,
        timezone = this.timezone,
        days = this.days.map { it.toWholeDayWeather() }
    )
}

fun WholeDayWeatherEntity.toWholeDayWeather(): WholeDayWeather {
    return WholeDayWeather(
        description = this.description,
        conditions = this.conditions,
        datetime = this.datetime,
        feelslike = this.feelslike,
        humidity = this.humidity,
        icon = this.icon,
        precip = this.precip,
        pressure = this.pressure,
        sunrise = this.sunrise,
        temp = this.temp,
        windspeed = this.windspeed
    )
}