package com.example.weathertrackingapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.weatherException.CustomException
import com.example.weathertrackingapp.common.observerPattern.Observer
import com.example.weathertrackingapp.presentation.fragments.currentWeather.UiEvent

abstract class BaseFragment<DataType>(private val fragmentId: Int) : Fragment(), Observer<UiEvent> {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(fragmentId, container, false)
    }

    override fun onUpdate(resultState: UiEvent) = requireActivity().runOnUiThread {
        when (resultState) {
            is UiEvent.ShowLoading -> showLoading(resultState.isLoading)
            is UiEvent.Success<*> -> bindViews(resultState.data as DataType)
            is UiEvent.ShowError -> showError(errorMessage = getFailureMessage(exception = resultState.error))
        }
    }

    abstract fun showLoading(isLoading: Boolean)
    abstract fun showError(errorMessage: String)
    abstract fun bindViews(data: DataType)

    fun getFailureMessage(exception: CustomException): String {
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
}