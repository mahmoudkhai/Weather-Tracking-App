package com.example.weathertrackingapp.common.weatherException

sealed class CustomException() : Exception() {

    sealed class LocationException() : CustomException() {
        data class UnKnownLocationException(
            override val message:String
        ) : CustomException()

    }
}
