package com.example.weathertrackingapp.common.weatherException

sealed class CustomException() : Exception() {

    sealed class NetworkException() : CustomException() {
        data class UnKnownNetworkException(override val message: String) : CustomException()

    }

    sealed class LocationException() : CustomException() {
        data class UnKnownLocationException(
            override val message: String,
        ) : CustomException()

    }
}
