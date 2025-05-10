package com.example.weathertrackingapp.presentation

import android.app.Application
import android.content.Context
import com.example.weathertrackingapp.presentation.delegationPattern.SystemUtil
import com.example.weathertrackingapp.presentation.delegationPattern.SystemUtilImpl
import com.example.weathertrackingapp.presentation.presentationUtil.BackgroundExecutor

class WeatherApp : Application(), SystemUtil by SystemUtilImpl() {

    override fun onCreate() {
        super.onCreate()
        weatherAppInstance = this
        systemLanguage = getSystemLanguage()
    }

    companion object {
        private lateinit var weatherAppInstance: WeatherApp
        lateinit var systemLanguage: String
        fun getAppContext(): Context = weatherAppInstance.applicationContext
    }
}