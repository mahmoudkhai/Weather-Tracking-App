package com.example.weathertrackingapp.common.weatherException

sealed class CustomException() : Exception() {

    sealed class NetworkException() : CustomException() {
        data class UnKnownNetworkException(override val message: String) : CustomException()
        data object UnAuthorizedException : NetworkException()
        data object PoorInternetConnectionException : NetworkException()

        data object NotFoundException : NetworkException()
        data object BadRequestException : NetworkException()
        data object InternalServerErrorException : NetworkException()
        data object TooManyRequests : NetworkException()

    }

    sealed class DataException() : CustomException() {
        data class ParsingException(
            override val message: String = "Parsing or Mapping Error",
        ) : DataException()
    }

    sealed class LocationException() : CustomException() {
        data class UnKnownLocationException(
            override val message: String,
        ) : CustomException()

    }
}
