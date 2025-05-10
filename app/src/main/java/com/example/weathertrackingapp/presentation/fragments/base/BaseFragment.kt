package com.example.weathertrackingapp.presentation.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.fragment.app.Fragment
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.common.observerPattern.Subscriber
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.presentation.delegationPattern.LocationUtil
import com.example.weathertrackingapp.presentation.delegationPattern.LocationUtilImpl
import com.example.weathertrackingapp.presentation.delegationPattern.UiUtil
import com.example.weathertrackingapp.presentation.delegationPattern.UiUtilImpl
import com.example.weathertrackingapp.presentation.presentationUtil.BackgroundExecutor
import com.example.weathertrackingapp.presentation.presentationUtil.UiEvent
import com.google.android.gms.location.LocationServices

abstract class BaseFragment<DataType>(private val fragmentId: Int) : Fragment(),
    Subscriber<UiEvent>,
    UiUtil by UiUtilImpl(),
    LocationUtil by LocationUtilImpl() {

    abstract val viewModel: BaseViewModel<UiEvent>
    lateinit var errorTextView: TextView
    lateinit var loadingProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = inflater.inflate(fragmentId, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setFusedLocationClient(LocationServices.getFusedLocationProviderClient(requireActivity()))
        fetchWeatherData()
    }

    abstract fun initializeViews(view: View)

    private fun fetchWeatherData() {
        try {
            requestFreshLocation { latLong ->
                registerObserverIntoViewModel()
                getDataInBackgroundThread(createWeatherRequest(latLong))
            }
        } catch (locationException: CustomException.LocationException) {
            showError(getFailureMessage(locationException))
        }
    }

    abstract fun getDataInBackgroundThread(weatherRequest: WeatherRequest)
    abstract fun createWeatherRequest(latLong: LatLong): WeatherRequest
    abstract fun registerObserverIntoViewModel()

    fun onRefresh() = fetchWeatherData()

    override fun onUpdate(domainState: UiEvent) = requireActivity().runOnUiThread {
        when (domainState) {
            is UiEvent.ShowLoading -> showLoading(domainState.isLoading)
            // i need to solve this to avoid casting exception at runtime if presentation model changed and i forget to edit it in fragment
            is UiEvent.SuccessWithFreshData<*> -> {
                hideErrorMessage()
                bindViews(domainState.data as DataType)
            }

            is UiEvent.SuccessWithCachedData<*> -> {
                bindViews(domainState.data as DataType)
            }

            is UiEvent.ShowError -> showError(
                errorMessage = domainState.error.joinToString(
                    getString(R.string.and)
                ) {
                    getFailureMessage(exception = it)
                })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun showError(errorMessage: String) = with(errorTextView) {
        visibility = View.VISIBLE
        text = errorMessage.plus(" ${getString(R.string.swipe_to_refresh)}")
    }

    private fun hideErrorMessage() = with(errorTextView) {
        visibility = View.GONE
    }

    abstract fun bindViews(data: DataType)

    private fun getFailureMessage(exception: CustomException): String {
        return when (exception) {
            is CustomException.NetworkException.UnKnownNetworkException -> {
                exception.errorMessage
                //                getString(R.string.unknown_network_error)
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

            is CustomException.DataException.UnSupportedTypeCasting -> {
                getString(R.string.un_supported_type_casting)
            }

            is CustomException.DataException.LocalInputOutputException -> getString(R.string.input_output_exception)

            is CustomException.DataException.NoCachedDataFound -> getString(R.string.no_cached_data_in_database)

            is CustomException.DataException.UnKnownDataException -> {
                getString(R.string.un_know_database_exception).plus(exception.exception.toString())
            }

            else -> {
                getString(R.string.general_error)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        BackgroundExecutor.cancelCurrentWork()
    }

    abstract fun unRegisterFragmentFromViewModel()
    override fun onDestroy() {
        super.onDestroy()
        destroyFusedLocationClient()
        unRegisterFragmentFromViewModel()
        BackgroundExecutor.cancelCurrentWork()
    }
}