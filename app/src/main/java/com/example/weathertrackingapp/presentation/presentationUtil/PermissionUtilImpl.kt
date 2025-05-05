package com.example.weathertrackingapp.presentation.presentationUtil

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager

class PermissionUtilImpl : PermissionUtil {
    override fun requestLocationPermission(activity: Activity) {
        activity.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun isLocationPermissionGranted(context: Context) =
        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    companion object {
        const val FIRST_REQUESTED_PERMISSION = 0
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

}