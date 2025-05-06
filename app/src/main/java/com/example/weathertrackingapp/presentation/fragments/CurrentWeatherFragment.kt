package com.example.weathertrackingapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.customState.ResultState
import com.example.weathertrackingapp.common.weatherException.CustomException
import com.example.weathertrackingapp.data.dataSources.remote.WeatherRemoteDSImpl
import com.example.weathertrackingapp.data.dataSources.remote.apiService.ApiServiceImpl
import com.example.weathertrackingapp.data.repository.WeatherRepositoryImpl
import com.example.weathertrackingapp.domain.model.CurrentConditions
import com.example.weathertrackingapp.domain.model.LatLong
import com.example.weathertrackingapp.domain.model.WeatherRequest
import com.example.weathertrackingapp.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathertrackingapp.domain.useCase.UseCaseObserver
import com.example.weathertrackingapp.presentation.presentationUtil.LocationUtil
import com.example.weathertrackingapp.presentation.presentationUtil.LocationUtilImpl
import com.example.weathertrackingapp.presentation.presentationUtil.PresentationCommonConstants
import com.google.android.gms.location.LocationServices

class CurrentWeatherFragment :
    Fragment(),
    UseCaseObserver<CurrentConditions>,
    LocationUtil by LocationUtilImpl() {

    private lateinit var systemLanguage: String
    private val getCurrentWeatherUseCase by lazy {
        GetCurrentWeatherUseCase(
            WeatherRepositoryImpl(
                WeatherRemoteDSImpl(ApiServiceImpl())
            )
        )
    }

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
        try {
            requestFreshLocation { latLong ->
                getCurrentWeatherUseCase.registerObserver(this)
                getCurrentWeatherInBackground(createWeatherRequest(latLong))
            }
        } catch (locationException: CustomException.LocationException) {
            // Handle location exception
        }
    }

    override fun onUpdate(resultState: ResultState<CurrentConditions>) {
        requireActivity().runOnUiThread {
            when (resultState) {
                is ResultState.IsLoading -> showLoading(resultState.isLoading)
                is ResultState.Success<CurrentConditions> -> bindCurrentConditions(resultState.data)
                is ResultState.Failure -> {
                    showError(
                        failureMessage = getFailureMessage(resultState.exception)
                    )
                }
            }
        }
    }

    private fun showError(failureMessage: String) {
        requireView().findViewById<TextView>(R.id.errorTextView).text =
            failureMessage.plus(
                " ${getString(R.string.swipe_to_refresh)}"
            )
    }

    private fun getFailureMessage(exception: CustomException): String {
        return when (exception) {
            is CustomException.NetworkException.UnKnownNetworkException -> {
                getString(R.string.unknown_network_error)
            }

            is CustomException.NetworkException.UnAuthorizedException -> {
                getString(R.string.unauthorized_error)
            }

            is CustomException.NetworkException.NoInternetConnection -> {
                getString(R.string.no_internet_connection)
            }

            is CustomException.NetworkException.NotFoundException -> {
                getString(R.string.not_found_error)
            }

            is CustomException.NetworkException.BadRequestException -> {
                getString(R.string.bad_request_error)
            }

            is CustomException.NetworkException.InternalServerErrorException -> {
                getString(R.string.internal_server_error)
            }

            is CustomException.NetworkException.TooManyRequests -> {
                getString(R.string.too_many_requests_error)
            }

            is CustomException.DataException.ParsingException -> {
                getString(R.string.parsing_error)
            }

            is CustomException.LocationException.UnKnownLocationException -> {
                getString(R.string.unknown_location_error)
            }

            else -> {
                getString(R.string.general_error)
            }
        }
    }



    private fun getCurrentWeatherInBackground(weatherRequest: WeatherRequest) {
        Thread {
            getCurrentWeatherUseCase.invoke(weatherRequest)
        }.start()
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

    private fun showLoading(loading: Boolean) {
        requireView().findViewById<ProgressBar>(R.id.progress_bar).visibility = if (loading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyFusedLocationClient()
        getCurrentWeatherUseCase.unregisterObserver(this)
    }

}