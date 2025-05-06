package com.example.weathertrackingapp.presentation.fragments.currentWeather

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.data.dataSources.remote.WeatherRemoteDSImpl
import com.example.weathertrackingapp.data.dataSources.remote.apiService.ApiServiceImpl
import com.example.weathertrackingapp.data.repository.WeatherRepositoryImpl
import com.example.weathertrackingapp.domain.model.CurrentConditions
import com.example.weathertrackingapp.domain.model.LatLong
import com.example.weathertrackingapp.domain.model.WeatherRequest
import com.example.weathertrackingapp.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathertrackingapp.presentation.fragments.BaseFragment
import com.example.weathertrackingapp.presentation.presentationUtil.LocationUtil
import com.example.weathertrackingapp.presentation.presentationUtil.LocationUtilImpl
import com.example.weathertrackingapp.presentation.presentationUtil.PresentationCommonConstants
import com.google.android.gms.location.LocationServices

class CurrentWeatherFragment :
    BaseFragment<CurrentConditions>(R.layout.fragment_current_weather),
    LocationUtil by LocationUtilImpl() {

    private lateinit var systemLanguage: String
    private val viewModel by lazy {
        CurrentWeatherViewModel(
            GetCurrentWeatherUseCase(
                WeatherRepositoryImpl(
                    WeatherRemoteDSImpl(ApiServiceImpl())
                )
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        systemLanguage = arguments?.getString(PresentationCommonConstants.SYSTEM_LANGUAGE)
            ?: PresentationCommonConstants.DEFAULT_LANGUAGE

        setFusedLocationClient(LocationServices.getFusedLocationProviderClient(requireActivity()))
        try {
            requestFreshLocation { latLong ->
                viewModel.registerObserver(this)
                getCurrentWeatherInBackground(createWeatherRequest(latLong))
            }
        } catch (locationException: CustomException.LocationException) {
            showError(getFailureMessage(locationException))
        }
    }

    private fun getCurrentWeatherInBackground(weatherRequest: WeatherRequest) = Thread {
        viewModel.processUserIntent(UserIntent.GetCurrentWeather(weatherRequest))
    }.start()

    private fun createWeatherRequest(latLong: LatLong): WeatherRequest = WeatherRequest(
        latLong = latLong,
        language = systemLanguage,
    )

//    override fun showLoading(isLoading: Boolean) {
//        requireView().findViewById<ProgressBar>(R.id.progress_bar).visibility = if (isLoading) {
//            View.VISIBLE
//        } else {
//            View.GONE
//        }
//    }

    override fun showError(errorMessage: String) {
        requireView().findViewById<TextView>(R.id.errorTextView).text =
            errorMessage.plus(
                " ${getString(R.string.swipe_to_refresh)}"
            )
    }

    override fun bindViews(data: CurrentConditions) {
        bindCurrentConditions(data)
    }

    private fun bindCurrentConditions(currentConditions: CurrentConditions) {
        val view = requireView()
        view.findViewById<TextView>(R.id.tvTemperature).text =
            currentConditions.temperature.toString()
        view.findViewById<TextView>(R.id.tvFeelsLike).text =
            currentConditions.feelsLike.toString()
        view.findViewById<TextView>(R.id.tvConditions).text = currentConditions.conditions
        view.findViewById<TextView>(R.id.tvIcon).text = currentConditions.icon
        view.findViewById<TextView>(R.id.tvHumidity).text =
            currentConditions.humidity.toString()
        view.findViewById<TextView>(R.id.tvCloudCover).text =
            currentConditions.cloudCover.toString()
        view.findViewById<TextView>(R.id.tvWindSpeed).text =
            currentConditions.windSpeed.toString()
        view.findViewById<TextView>(R.id.tvUvIndex).text =
            currentConditions.uvIndex.toString()
        view.findViewById<TextView>(R.id.tvSunrise).text = currentConditions.sunrise
        view.findViewById<TextView>(R.id.tvSunset).text = currentConditions.sunset
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyFusedLocationClient()
        viewModel.unregisterObserver(this)
        viewModel.onDestroy()
    }

}