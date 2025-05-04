package com.example.weathertrackingapp.domain

import android.content.Context
import android.location.Location

interface LocationRepository {
    fun getLocation(context:Context): Location?
}
