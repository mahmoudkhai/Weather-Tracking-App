package com.example.weathertrackingapp.presentation.presentationUtil

import com.example.weathertrackingapp.common.appState.AppState
import com.example.weathertrackingapp.domain.model.LatLong
import com.google.android.gms.location.FusedLocationProviderClient

/**
 * Interface for handling location-related operations in a decoupled, reusable way.
 *
 * ## Usage:
 * 1. **Call [setFusedLocationClient] first** to initialize the required FusedLocationProviderClient before using other methods.
 * 2. Use [requestFreshLocation] to fetch the user's current location as a one-time request.
 * 3. **Call [destroyFusedLocationClient] in your Activity or Fragment's `onDestroy()`** to release any held references and avoid memory leaks.
 *
 * ⚠️ **NOTE:** Failure to call [setFusedLocationClient] before [requestFreshLocation] may result in an uninitialized client.
 * Also, always call [destroyFusedLocationClient] when the component is destroyed to avoid memory leaks.
 */
interface LocationUtil {
    fun setFusedLocationClient(fusedLocationClient: FusedLocationProviderClient)
    fun requestFreshLocation(callBack: (AppState<LatLong>) -> Unit)
    fun destroyFusedLocationClient()
}