package com.example.weathertrackingapp.data.dataSources.remote.apiService

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.weatherException.CustomException
import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.model.WeatherRequest
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ApiServiceImpl : ApiService {

    override fun getCurrentWeather(weatherRequest: WeatherRequest): CurrentConditionsDto {
        val lat = weatherRequest.latLong.latitude
        val long = weatherRequest.latLong.longitude
        val language = weatherRequest.language
        val urlString =
            "$BASE_URL/$lat,$long/today?unitGroup=metric&lang=$language&include=current&key=$API_KEY&contentType=json"
        try {
            val connection = createHttpGetRequest(urlString)
            Log.d(TAG, "connection opened successfully with request method = GET")

            val responseCode = connection.responseCode
            Log.d(TAG, "Response Code = $responseCode")
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw CustomException.NetworkException.UnKnownNetworkException("Error Code = $responseCode")
            }
            val apiResponse: String = getApiResponseAsString(connection)
            Log.d(TAG, "opened buffer reader to get the response")

            val wrappedResponse = JSONObject(apiResponse)
            val currentConditionsJson = wrappedResponse.getJSONObject("currentConditions")

            return CurrentConditionsDto(
                cloudcover = currentConditionsJson.getDouble("cloudcover"),
                conditions = currentConditionsJson.getString("conditions"),
                datetime = currentConditionsJson.getString("datetime"),
                datetimeEpoch = currentConditionsJson.getInt("datetimeEpoch"),
                dew = currentConditionsJson.getDouble("dew"),
                feelslike = currentConditionsJson.getDouble("feelslike"),
                humidity = currentConditionsJson.getDouble("humidity"),
                icon = currentConditionsJson.getString("icon"),
                moonphase = currentConditionsJson.getDouble("moonphase"),
                precip = currentConditionsJson.opt("precip"),  // nullable
                precipprob = currentConditionsJson.getDouble("precipprob"),
                preciptype = currentConditionsJson.opt("preciptype"), // nullable
                pressure = currentConditionsJson.getDouble("pressure"),
                snow = currentConditionsJson.getDouble("snow"),
                snowdepth = currentConditionsJson.getDouble("snowdepth"),
                solarenergy = currentConditionsJson.getDouble("solarenergy"),
                solarradiation = currentConditionsJson.getDouble("solarradiation"),
                source = currentConditionsJson.getString("source"),
                stations = currentConditionsJson.getJSONArray("stations").let { array ->
                    List(array.length()) { index -> array.getString(index) }
                },
                sunrise = currentConditionsJson.getString("sunrise"),
                sunriseEpoch = currentConditionsJson.getInt("sunriseEpoch"),
                sunset = currentConditionsJson.getString("sunset"),
                sunsetEpoch = currentConditionsJson.getInt("sunsetEpoch"),
                temp = currentConditionsJson.getDouble("temp"),
                uvindex = currentConditionsJson.getDouble("uvindex"),
                visibility = currentConditionsJson.getDouble("visibility"),
                winddir = currentConditionsJson.getDouble("winddir"),
                windgust = currentConditionsJson.opt("windgust"),  // nullable
                windspeed = currentConditionsJson.getDouble("windspeed")
            ).also { Log.d(TAG, "Current Conditions = $it") }

        } catch (e: Exception) {
            throw CustomException.NetworkException.UnKnownNetworkException(
                "Failed to get Code msg,exception stackTrace = ${e.toString()} cause = ${e.cause}" +
                        ", message = ${e.message}"
            )
        }

    }

    private fun getApiResponseAsString(connection: HttpsURLConnection): String {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readText()
        reader.close()
        return response
    }

    private fun createHttpGetRequest(urlString: String): HttpsURLConnection {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpsURLConnection
        connection.requestMethod = "GET"
        return connection
    }

    companion object {
        private const val BASE_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
        private const val API_KEY = "RA2BDAYLZBGTLU4BCQZE4DE8E"
        const val DTO_OBJECT_NAME = "CurrentConditionsDto"

    }
}