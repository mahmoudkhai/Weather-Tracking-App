package com.example.weathertrackingapp.domain.dataSources.local

import android.location.Location

interface LocationDataSource {
    fun getCurrentLocation(callback: (Location) -> Unit)

}