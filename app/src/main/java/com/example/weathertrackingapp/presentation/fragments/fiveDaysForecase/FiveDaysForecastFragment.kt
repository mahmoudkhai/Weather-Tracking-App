package com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase

import android.os.Bundle
import android.view.View
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.di.AppDependenciesProvider
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.presentation.WeatherApp
import com.example.weathertrackingapp.presentation.fragments.base.BaseFragment
import com.example.weathertrackingapp.presentation.model.FiveDaysForecast

class FiveDaysForecastFragment :
    BaseFragment<FiveDaysForecast>(R.layout.fragment_five_days_frocast) {

    override val viewModel by lazy {
        AppDependenciesProvider.provideFiveDaysForecastViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initializeViews(view: View) {
        errorTextView = view.findViewById(R.id.errorTextView)
        progressBar = view.findViewById(R.id.progress_bar)
    }


    override fun registerObserverIntoViewModel() = viewModel.registerSubscriber(this)

    override fun createWeatherRequest(latLong: LatLong) =
        WeatherRequest(latLong = latLong, language = WeatherApp.systemLanguage)

    override fun getDataInBackgroundThread(weatherRequest: WeatherRequest) =
        Thread {
            viewModel.processUserIntent(
                FiveDaysForecastScreenContract.Intent.GetFiveDaysForecast(
                    weatherRequest
                )
            )
        }.start()


    override fun bindViews(data: FiveDaysForecast) {

    }

    override fun unRegisterFragmentFromViewModel() = viewModel.unregisterSubscriber(this)
}