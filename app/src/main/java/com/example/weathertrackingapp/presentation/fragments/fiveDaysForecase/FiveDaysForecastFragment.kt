package com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase

import android.util.Log
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecast
import com.example.weathertrackingapp.presentation.fragments.base.BaseFragment
import com.example.weathertrackingapp.common.di.AppDependenciesProvider

class FiveDaysForecastFragment :
    BaseFragment<FiveDaysForecast>(R.layout.fragment_five_days_frocast) {

    override val viewModel by lazy {
        AppDependenciesProvider.provideFiveDaysForecastViewModel()
    }

    override fun registerObserverIntoViewModel() = viewModel.registerObserver(this)

    override fun createWeatherRequest(latLong: LatLong) =
        WeatherRequest(latLong = latLong, language = systemLanguage)

    override fun getDataInBackgroundThread(weatherRequest: WeatherRequest) =
        Thread {
            viewModel.processUserIntent(
                FiveDaysForecastScreenContract.Intent.GetFiveDaysForecast(
                    weatherRequest
                )
            )
        }.start()

    override fun showError(errorMessage: String) {
        Log.d(TAG, "Error Message = $errorMessage")
    }

    override fun bindViews(data: FiveDaysForecast) {

    }

    override fun unRegisterFragmentFromViewModel() = viewModel.unregisterObserver(this)
}