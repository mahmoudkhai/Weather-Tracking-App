package com.example.weathertrackingapp.presentation.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.presentation.fragments.currentWeather.CurrentWeatherFragment
import com.example.weathertrackingapp.presentation.delegationPattern.PermissionUtil
import com.example.weathertrackingapp.presentation.delegationPattern.PermissionUtilImpl
import com.example.weathertrackingapp.presentation.presentationUtil.PresentationConstants
import com.example.weathertrackingapp.presentation.delegationPattern.SystemUtil
import com.example.weathertrackingapp.presentation.delegationPattern.SystemUtilImpl
import com.example.weathertrackingapp.presentation.delegationPattern.UiUtil
import com.example.weathertrackingapp.presentation.delegationPattern.UiUtilImpl

/**
 * MainActivity delegates utility responsibilities to implementation classes using Kotlin's delegation pattern.
 *
 * ## Why delegation?
 * Delegation is used here to:
 * - **Promote composition over inheritance**: Instead of subclassing utility classes or making MainActivity implement their logic directly,
 *   it delegates functionality to standalone utility implementations.
 * - **Encapsulate and reuse functionality**: Each utility class (UiUtilImpl, LocationUtilImpl, PermissionUtilImpl) encapsulates
 *   a specific concern (UI helpers, location handling, permission management) making them reusable and testable independently.
 * - **Reduce boilerplate in MainActivity**: MainActivity doesn't need to manually forward method calls to utility instances —
 *   the delegation syntax automatically exposes the interface methods as if they were implemented directly in MainActivity.
 *
 * ## Benefit:
 * This pattern keeps MainActivity focused on high-level orchestration while delegating specialized functionality to
 * dedicated, testable classes, improving modularity, readability, and maintainability.
 *
 * Example:
 * Instead of `uiUtil.showToast()`, we can directly call `showToast()` from MainActivity as if it were a native method.
 */
class MainActivity : AppCompatActivity(),
    UiUtil by UiUtilImpl(),
    SystemUtil by SystemUtilImpl(),
    PermissionUtil by PermissionUtilImpl() {

    private var dialog: Dialog? = null
    private var comingBackFromSettings = false
    private val currentWeatherFragment: CurrentWeatherFragment by lazy {
        CurrentWeatherFragment()
    }


    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (!isLocationPermissionGranted(this)) requestLocationPermission(this)
        else navigateToCurrentWeatherFragmentWithBundle()
    }


    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onResume() {
        super.onResume()
        releaseDialog()

        if (comingBackFromSettings) {
            comingBackFromSettings = false
            if (isLocationPermissionGranted(this)) {
                navigateToCurrentWeatherFragmentWithBundle()
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
            if (isPermissionGranted(grantResults)) navigateToCurrentWeatherFragmentWithBundle()
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

    private fun navigateToCurrentWeatherFragmentWithBundle() {
        val bundle = Bundle()
        bundle.putString(PresentationConstants.SYSTEM_LANGUAGE, getSystemLanguage())
        currentWeatherFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, currentWeatherFragment)
            .commit()
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


}
