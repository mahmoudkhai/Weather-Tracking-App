package com.example.weathertrackingapp.presentation.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.presentation.delegationPattern.LocationUtil
import com.example.weathertrackingapp.presentation.delegationPattern.LocationUtilImpl
import com.example.weathertrackingapp.presentation.presentationUtil.PresentationConstants
import com.example.weathertrackingapp.presentation.presentationUtil.UiEvent
import com.google.android.gms.location.LocationServices

abstract class BaseFragment<DataType>(private val fragmentId: Int) : Fragment(), Observer<UiEvent>,
    LocationUtil by LocationUtilImpl() {

    abstract val viewModel: BaseViewModel<UiEvent>
    lateinit var systemLanguage: String
    private var loadingView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(fragmentId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        systemLanguage = savedInstanceState?.getString(PresentationConstants.SYSTEM_LANGUAGE)
            ?: PresentationConstants.DEFAULT_LANGUAGE
        setFusedLocationClient(LocationServices.getFusedLocationProviderClient(requireActivity()))
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

    override fun onUpdate(domainState: UiEvent) = requireActivity().runOnUiThread {
        when (domainState) {
            is UiEvent.ShowLoading -> showLoading(domainState.isLoading)
            is UiEvent.Success<*> -> bindViews(domainState.data as DataType)
            is UiEvent.ShowError -> showError(errorMessage = getFailureMessage(exception = domainState.error))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        val loadingView = view?.findViewById<View>(R.id.loading_view)
        if (loadingView != null) {
            val progressBar = loadingView.findViewById<ProgressBar>(R.id.loading_view)
            progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    abstract fun showError(errorMessage: String)
    abstract fun bindViews(data: DataType)

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

            CustomException.DataException.UnSupportedTypeCasting -> {
                getString(R.string.un_supported_type_casting)
            }
            else -> {
                getString(R.string.general_error)
            }
        }
    }

    abstract fun unRegisterFragmentFromViewModel()
    override fun onDestroy() {
        super.onDestroy()
        destroyFusedLocationClient()
        unRegisterFragmentFromViewModel()
    }
}