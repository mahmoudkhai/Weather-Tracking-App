package com.example.weathertrackingapp.presentation.delegationPattern

import android.app.Activity
import android.content.Context

interface PermissionUtil {
    fun requestLocationPermission(activity: Activity)
    fun isLocationPermissionGranted(context: Context): Boolean
}
