package com.example.weathertrackingapp.presentation.fragments.currentWeather

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.di.AppDependenciesProvider
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.presentation.WeatherApp
import com.example.weathertrackingapp.presentation.fragments.base.BaseFragment
import com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase.FiveDaysForecastFragment
import com.example.weathertrackingapp.presentation.model.CurrentWeather
import com.example.weathertrackingapp.presentation.presentationUtil.BackgroundExecutor
import com.example.weathertrackingapp.presentation.presentationUtil.bindOrHide

class CurrentWeatherFragment : BaseFragment<CurrentWeather>(R.layout.fragment_current_weather) {

    private lateinit var swipeProgressBar: ProgressBar
    private lateinit var rootLayout: ViewGroup
    override val viewModel by lazy {
        AppDependenciesProvider.provideCurrentWeatherViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enableSwipeToRefreshFeature(rootLayout, swipeProgressBar, ::onRefresh)
    }

    override fun initializeViews(view: View) {
        errorTextView = view.findViewById(R.id.errorTextView)
        loadingProgressBar = view.findViewById(R.id.progress_bar)
        swipeProgressBar = view.findViewById(R.id.swipe_progress_bar)
        rootLayout = view.findViewById(R.id.root_layout)
        view.findViewById<Button>(R.id.btn_navigate_to_forecast).setOnClickListener {
            navigateToFiveDaysForecastFragment()
        }
    }

    private fun navigateToFiveDaysForecastFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FiveDaysForecastFragment()).addToBackStack(null)
            .commit()
    }

    override fun registerObserverIntoViewModel() = viewModel.registerSubscriber(this)

    override fun getDataInBackgroundThread(weatherRequest: WeatherRequest) =
        BackgroundExecutor.run {
            viewModel.processUserIntent(
                CurrentWeatherScreenContract.Intent.GetCurrentWeather(
                    weatherRequest
                )
            )
        }

    override fun createWeatherRequest(latLong: LatLong): WeatherRequest = WeatherRequest(
        latLong = latLong,
        language = WeatherApp.systemLanguage,
    )

    override fun bindViews(data: CurrentWeather) {
        bindCurrentConditions(data)
    }

    private fun bindCurrentConditions(currentWeather: CurrentWeather) {
        val view = requireView()
        val conditions = currentWeather.currentConditions

        // Existing bindings
        view.findViewById<TextView>(R.id.tvTemperature)
            .bindOrHide(conditions.temperature?.toString())
        view.findViewById<TextView>(R.id.tvFeelsLike).bindOrHide(conditions.feelsLike?.toString())
        view.findViewById<TextView>(R.id.tvConditions).bindOrHide(conditions.conditions)
        view.findViewById<TextView>(R.id.tvIcon).bindOrHide(conditions.icon)
        view.findViewById<TextView>(R.id.tvPressure).bindOrHide(conditions.pressure?.toString())
        view.findViewById<TextView>(R.id.tvHumidity).bindOrHide(conditions.humidity?.toString())
        view.findViewById<TextView>(R.id.tvCloudCover).bindOrHide(conditions.cloudCover?.toString())
        view.findViewById<TextView>(R.id.tvWindSpeed).bindOrHide(conditions.windSpeed?.toString())
        view.findViewById<TextView>(R.id.tvUvIndex).bindOrHide(conditions.uvIndex?.toString())
        view.findViewById<TextView>(R.id.tvSunrise).bindOrHide(conditions.sunrise)
        view.findViewById<TextView>(R.id.tvSunset).bindOrHide(conditions.sunset)
        view.findViewById<TextView>(R.id.tvDate).bindOrHide(conditions.dateTime)
        // Top-level CurrentWeather fields
        view.findViewById<TextView>(R.id.tvResolvedAddress)
            .bindOrHide(currentWeather.resolvedAddress)
        view.findViewById<TextView>(R.id.tvTimeZone).bindOrHide(currentWeather.timeZone)
        view.findViewById<TextView>(R.id.tvAddress).bindOrHide(currentWeather.address)

        // New whole-day bindings (assuming you get it from currentWeather.wholeDay or similar)
        val wholeDay = currentWeather.wholeDayWeather  // adjust based on your actual model

        view.findViewById<TextView>(R.id.tvWholeDayDescription).bindOrHide(wholeDay.description)
        view.findViewById<TextView>(R.id.tvWholeDayDatetime).bindOrHide(wholeDay.datetime)
        view.findViewById<TextView>(R.id.tvWholeDayIcon).bindOrHide(wholeDay.icon)
        view.findViewById<TextView>(R.id.tvWholeDayConditions).bindOrHide(wholeDay.conditions)
        view.findViewById<TextView>(R.id.tvWholeDayFeelsLike)
            .bindOrHide(wholeDay.feelslike?.toString())
        view.findViewById<TextView>(R.id.tvWholeDayHumidity)
            .bindOrHide(wholeDay.humidity?.toString())
        view.findViewById<TextView>(R.id.tvWholeDayPrecip).bindOrHide(wholeDay.precip?.toString())
        view.findViewById<TextView>(R.id.tvWholeDayPressure)
            .bindOrHide(wholeDay.pressure?.toString())
        view.findViewById<TextView>(R.id.tvWholeDayTemperature)
            .bindOrHide(wholeDay.temp?.toString())
        view.findViewById<TextView>(R.id.tvWholeDayWindSpeed)
            .bindOrHide(wholeDay.windspeed?.toString())
    }


    override fun unRegisterFragmentFromViewModel() {
        viewModel.unregisterSubscriber(this)
    }
}