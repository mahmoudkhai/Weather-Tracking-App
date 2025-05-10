package com.example.weathertrackingapp.presentation.delegationPattern

import android.Manifest
import androidx.annotation.RequiresPermission
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationUtilImpl() : LocationUtil {
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun setFusedLocationClient(fusedLocationClient: FusedLocationProviderClient) {
        if (this.fusedLocationClient == null) {
            this.fusedLocationClient = fusedLocationClient
        }
    }


    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun requestFreshLocation(callBack: (LatLong) -> Unit) {
        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient?.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token
        )?.addOnSuccessListener { location ->
            if (location != null) {
                callBack(LatLong(location.latitude, location.longitude))
            } else {
                throw CustomException.LocationException.UnKnownLocationException("Location is null")
                // Maybe inform user to try again / move outdoors
            }
        }?.addOnFailureListener { exception ->
            throw CustomException.LocationException.UnKnownLocationException(
                exception.message ?: "Unknown error"
            )
        }
    }

    override fun destroyFusedLocationClient() {
        fusedLocationClient = null
    }
}