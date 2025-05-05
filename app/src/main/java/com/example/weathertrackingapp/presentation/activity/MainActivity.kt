package com.example.weathertrackingapp.presentation.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.appState.AppState
import com.example.weathertrackingapp.common.weatherException.CustomException
import com.example.weathertrackingapp.databinding.ActivityMainBinding
import com.example.weathertrackingapp.domain.model.LatLong
import com.example.weathertrackingapp.presentation.presentationUtil.LocationUtil
import com.example.weathertrackingapp.presentation.presentationUtil.LocationUtilImpl
import com.example.weathertrackingapp.presentation.presentationUtil.PermissionUtil
import com.example.weathertrackingapp.presentation.presentationUtil.PermissionUtilImpl
import com.example.weathertrackingapp.presentation.presentationUtil.UiUtil
import com.example.weathertrackingapp.presentation.presentationUtil.UiUtilImpl
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(),
    UiUtil by UiUtilImpl(),
    LocationUtil by LocationUtilImpl(),
    PermissionUtil by PermissionUtilImpl() {

    private lateinit var binding: ActivityMainBinding
    private var dialog: Dialog? = null
    private var comingBackFromSettings = false


    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setFusedLocationClient(LocationServices.getFusedLocationProviderClient(this))
        if (isLocationPermissionGranted(this)) requestFreshLocation(::handleLocationState)
        else requestLocationPermission(this)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onResume() {
        super.onResume()
        releaseDialog()

        if (comingBackFromSettings) {
            comingBackFromSettings = false
            if (isLocationPermissionGranted(this)) {
                requestFreshLocation(::handleLocationState)
            } else {
                Log.d(TAG, "onResume: Location is still deniend")
                showGoToSettingsDialog()
            }
        }

    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtilImpl.LOCATION_PERMISSION_REQUEST_CODE) {
            if (isPermissionGranted(grantResults)) requestFreshLocation(::handleLocationState)
            else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // User denied WITHOUT "Don't ask again" → can show rationale and re-request
                    showPermissionExplanationDialog()
                } else {
                    // User denied WITH "Don't ask again" → must guide user to Settings
                    showGoToSettingsDialog()
                }
            }
        }
    }

    //region --------------------Private functions--------------------

    private fun handleLocationState(state: AppState<LatLong>) {
        when (state) {
            is AppState.Success -> {
                Log.d(TAG, "Lat and long are =  ${state.data.toString()}")
            }

            is AppState.Failure -> when (state.exception) {
                is CustomException.LocationException.UnKnownLocationException -> {
                    Log.d(TAG, "Location error: ${state.exception.message}")
                    Toast.makeText(this, state.exception.message, Toast.LENGTH_LONG).show()
                }
            }

            is AppState.IsLoading -> {}
        }
    }

    private fun isPermissionGranted(grantResults: IntArray) =
        grantResults.isNotEmpty() && grantResults[PermissionUtilImpl.FIRST_REQUESTED_PERMISSION] == PackageManager.PERMISSION_GRANTED

    private fun openSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun showPermissionExplanationDialog() = showDialog(
        context = this,
        title = getString(R.string.permission_needed),
        message = getString(R.string.please_allow_location_access),
        positiveButton = getString(R.string.allow),
        negativeButton = getString(R.string.cancel),
        onPositiveButtonClick = {
            // Retry requesting permission
            requestLocationPermission(this)
        },
        onNegativeButtonClick = {
            this.finish()
        })

    private fun showGoToSettingsDialog() {
        comingBackFromSettings = true
        dialog = showDialog(
            context = this,
            title = getString(R.string.permission_needed),
            message = getString(R.string.go_to_setting_dialog),
            positiveButton = getString(R.string.go_to_settings),
            negativeButton = getString(R.string.cancel),
            onPositiveButtonClick = {
                // Open app settings
                openSetting()
            },
            onNegativeButtonClick = {
                this.finish()
            })
    }

    private fun releaseDialog() {
        dialog?.dismiss()
        dialog = null
    }
    //endregion --------------------Private functions--------------------


    override fun onDestroy() {
        super.onDestroy()
        destroyFusedLocationClient()
    }

    companion object {
        private const val TAG = "MAIN_ACTIVITY_TAG"
    }

}
