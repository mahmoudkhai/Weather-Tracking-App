package com.example.weathertrackingapp.presentation.presentationUtil

import com.example.weathertrackingapp.common.appState.AppState
import com.example.weathertrackingapp.domain.model.LatLong
import com.google.android.gms.location.FusedLocationProviderClient

interface LocationUtil {
    fun setFusedLocationClient(fusedLocationClient: FusedLocationProviderClient)
    fun requestFreshLocation(callBack: (AppState<LatLong>) -> Unit)
    fun destroyFusedLocationClient()
}