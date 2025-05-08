package com.example.weathertrackingapp.presentation.fragments.currentWeather

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.di.AppDependenciesProvider
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.presentation.WeatherApp
import com.example.weathertrackingapp.presentation.delegationPattern.UiUtil
import com.example.weathertrackingapp.presentation.delegationPattern.UiUtilImpl
import com.example.weathertrackingapp.presentation.fragments.base.BaseFragment
import com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase.FiveDaysForecastFragment
import com.example.weathertrackingapp.presentation.model.CurrentWeather

class CurrentWeatherFragment : UiUtil by UiUtilImpl(),
    BaseFragment<CurrentWeather>(R.layout.fragment_current_weather) {

    override val viewModel by lazy {
        AppDependenciesProvider.provideCurrentWeatherViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableSwipeToRefreshFeature(
            view.findViewById(R.id.root_layout),
            view.findViewById(R.id.swipe_progress_bar),
            ::onRefresh
        )
        requireView().findViewById<Button>(R.id.btn_navigate_to_forecast).setOnClickListener {
            navigateToFiveDaysForecastFragment()
        }
    }

    override fun initializeViews(view: View) {
        errorTextView = view.findViewById(R.id.errorTextView)
        progressBar = view.findViewById(R.id.progress_bar)
    }

    private fun navigateToFiveDaysForecastFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FiveDaysForecastFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun registerObserverIntoViewModel() =
        viewModel.registerSubscriber(this)

    override fun getDataInBackgroundThread(weatherRequest: WeatherRequest) =
        Thread {
            viewModel.processUserIntent(
                CurrentWeatherScreenContract.Intent.GetCurrentWeather(
                    weatherRequest
                )
            )
        }.start()

    override fun createWeatherRequest(latLong: LatLong): WeatherRequest =
        WeatherRequest(
            latLong = latLong,
            language = WeatherApp.systemLanguage,
        )

    override fun bindViews(data: CurrentWeather) {
        bindCurrentConditions(data)
    }

    private fun bindCurrentConditions(currentWeather: CurrentWeather) {
        val view = requireView()
        view.findViewById<TextView>(R.id.tvTemperature).text =
            currentWeather.temperature.toString()
        view.findViewById<TextView>(R.id.tvFeelsLike).text =
            currentWeather.feelsLike.toString()
        view.findViewById<TextView>(R.id.tvConditions).text = currentWeather.conditions
        view.findViewById<TextView>(R.id.tvIcon).text = currentWeather.icon
        view.findViewById<TextView>(R.id.tvHumidity).text =
            currentWeather.humidity.toString()
        view.findViewById<TextView>(R.id.tvCloudCover).text =
            currentWeather.cloudCover.toString()
        view.findViewById<TextView>(R.id.tvWindSpeed).text =
            currentWeather.windSpeed.toString()
        view.findViewById<TextView>(R.id.tvUvIndex).text =
            currentWeather.uvIndex.toString()
        view.findViewById<TextView>(R.id.tvSunrise).text = currentWeather.sunrise
        view.findViewById<TextView>(R.id.tvSunset).text = currentWeather.sunset
    }

    override fun unRegisterFragmentFromViewModel() {
        viewModel.unregisterSubscriber(this)
    }
}