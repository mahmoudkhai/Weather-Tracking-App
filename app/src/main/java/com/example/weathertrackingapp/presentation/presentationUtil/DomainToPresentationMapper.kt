package com.example.weathertrackingapp.presentation.presentationUtil

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentConditionsEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.CurrentWeatherEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecastEntity
import com.example.weathertrackingapp.domain.entity.responseEntities.WholeDayWeatherEntity
import com.example.weathertrackingapp.presentation.model.CurrentConditions
import com.example.weathertrackingapp.presentation.model.CurrentWeather
import com.example.weathertrackingapp.presentation.model.FiveDaysForecast
import com.example.weathertrackingapp.presentation.model.WholeDayWeather

// here i used another approach instead of data layer approach that i applied, i know this is inconsistent but i'm learning.
// and i feel this approach is better than creating an Object for mapping because Object will live as long as the app live.
fun CurrentConditionsEntity.toCurrentWeather(): CurrentConditions {
    Log.d(TAG, "current weather Entity = $this")
    return CurrentConditions(
        pressure = this.pressure,
        temperature = this.temperature,
        dateTime = this.dateTime,
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

fun CurrentWeatherEntity.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        queryCost = this.queryCost,
        resolvedAddress = this.resolvedAddress,
        timeZone = this.timeZone,
        address = this.address,
        currentConditions = this.currentConditions!!.toCurrentWeather(),
        wholeDayWeather = this.wholeDayWeather.toWholeDayWeather()
    )
}

fun FiveDaysForecastEntity.toFiveDaysForecast(): FiveDaysForecast {
    return FiveDaysForecast(
        resolvedAddress = this.resolvedAddress,
        address = this.address,
        timezone = this.timezone,
        days = this.days?.map { it.toWholeDayWeather() }
    )
}

fun WholeDayWeatherEntity.toWholeDayWeather(): WholeDayWeather {
    return WholeDayWeather(
        description = this.description,
        datetime = this.datetime,
        icon = this.icon,
        conditions = this.conditions,
        feelslike = this.feelslike,
        humidity = this.humidity,
        precip = this.precip,
        pressure = this.pressure,
        temp = this.temp,
        windspeed = this.windspeed
    )
}