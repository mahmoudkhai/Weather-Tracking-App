package com.example.weathertrackingapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.appState.ResultState
import com.example.weathertrackingapp.common.weatherException.CustomException
import com.example.weathertrackingapp.data.dataSources.remote.WeatherRemoteDSImpl
import com.example.weathertrackingapp.data.dataSources.remote.apiService.ApiServiceImpl
import com.example.weathertrackingapp.data.repository.WeatherRepositoryImpl
import com.example.weathertrackingapp.domain.model.CurrentConditions
import com.example.weathertrackingapp.domain.model.LatLong
import com.example.weathertrackingapp.domain.model.WeatherRequest
import com.example.weathertrackingapp.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathertrackingapp.presentation.presentationUtil.LocationUtil
import com.example.weathertrackingapp.presentation.presentationUtil.LocationUtilImpl
import com.example.weathertrackingapp.presentation.presentationUtil.PresentationCommonConstants
import com.google.android.gms.location.LocationServices

class CurrentWeatherFragment :
    Fragment(),
    LocationUtil by LocationUtilImpl() {

    private lateinit var systemLanguage: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_current_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        systemLanguage = arguments?.getString(PresentationCommonConstants.SYSTEM_LANGUAGE)
            ?: PresentationCommonConstants.DEFAULT_LANGUAGE

        setFusedLocationClient(LocationServices.getFusedLocationProviderClient(requireActivity()))
        requestFreshLocation(::handleLocationState)
    }

    private fun handleLocationState(state: ResultState<LatLong>) = handleState(
        state = state,
        onSuccess = { latLong ->
            getCurrentWeatherInBackground(createWeatherRequest(latLong), ::handleWeatherState)
        }, onFailure = { customException -> })


    private fun handleWeatherState(state: ResultState<CurrentConditions>) = handleState(
        state,
        onSuccess = ::bindCurrentConditions,
        onFailure = { TODO() },
        onLoading = { TODO() },
    )

    private fun getCurrentWeatherInBackground(
        weatherRequest: WeatherRequest,
        onResult: (ResultState<CurrentConditions>) -> Unit,
    ) =
        Thread {
            val result = GetCurrentWeatherUseCase(
                WeatherRepositoryImpl(
                    WeatherRemoteDSImpl(ApiServiceImpl())
                )
            ).invoke(weatherRequest)

            requireActivity().runOnUiThread { onResult(result) }

        }.start()

    private fun <T> handleState(
        state: ResultState<T>,
        onSuccess: (T) -> Unit,
        onFailure: (CustomException) -> Unit,
        onLoading: () -> Unit = {},
    ) = when (state) {
        is ResultState.Success<T> -> onSuccess(state.data)
        is ResultState.Failure -> onFailure(state.exception)
        is ResultState.IsLoading -> onLoading()
    }

    private fun createWeatherRequest(latLong: LatLong): WeatherRequest = WeatherRequest(
        latLong = latLong,
        language = systemLanguage,
    )

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
    }

}