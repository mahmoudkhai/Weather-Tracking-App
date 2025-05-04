package com.example.weathertrackingapp.presentation.presentationUtil

import android.Manifest
import androidx.annotation.RequiresPermission
import com.example.weathertrackingapp.common.appState.AppState
import com.example.weathertrackingapp.common.weatherException.CustomException
import com.example.weathertrackingapp.domain.model.LatLong
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
    override fun requestFreshLocation(callBack: (AppState<LatLong>) -> Unit) {
        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient?.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token
        )?.addOnSuccessListener { location ->
            if (location != null) {
                callBack(AppState.Success(LatLong(location.latitude, location.longitude)))
            } else {
                AppState.Failure(CustomException.LocationException.UnKnownLocationException("Location is null"))
                // Maybe inform user to try again / move outdoors
            }
        }?.addOnFailureListener { exception ->
            AppState.Failure(
                CustomException.LocationException.UnKnownLocationException(
                    exception.message ?: "Unknown error"
                )
            )
        }
    }

    override fun destroyFusedLocationClient() {
        fusedLocationClient = null
    }
}