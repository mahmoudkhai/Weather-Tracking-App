package com.example.weathertrackingapp.common.customException

sealed class CustomException() : Exception() {

    sealed class NetworkException() : CustomException() {
        data class UnKnownNetworkException(val errorMessage:String) : CustomException()
        data object UnAuthorizedException : NetworkException()
        data object NoInternetConnection : NetworkException()
        data object NotFoundException : NetworkException()
        data object BadRequestException : NetworkException()
        data object InternalServerErrorException : NetworkException()
        data object TooManyRequests : NetworkException()
    }

    sealed class DataException() : CustomException() {
        data object ParsingException : DataException()
        data object UnSupportedTypeCasting : DataException()
        data object LocalInputOutputException : DataException()
        data class NoCachedDataFound(val e:Exception) : DataException()
        data class UnKnownDataException(val exception: Exception) : DataException()
    }

    sealed class LocationException() : CustomException() {
        data class UnKnownLocationException(
            override val message: String,
        ) : CustomException()

    }
}