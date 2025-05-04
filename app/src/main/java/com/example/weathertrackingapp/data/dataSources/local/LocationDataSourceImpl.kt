package com.example.weathertrackingapp.data.dataSources.local

import android.content.Context
import android.location.Location
import com.example.weathertrackingapp.domain.dataSources.local.LocationDataSource
import com.google.android.gms.location.LocationServices

class LocationDataSourceImpl(private val context: Context) : LocationDataSource {
    override fun getCurrentLocation(callback: (Location) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->

            }
            .addOnFailureListener { }
    }


}