package com.example.weathertrackingapp.data.dataSources.remote.apiService

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.domain.model.WeatherRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ApiServiceImpl : ApiService {

    override fun getCurrentWeatherConditions(
        weatherRequest: WeatherRequest,
        baseUrl: String,
        startDate: String,
        endDate: String,
        apiKey: String,
        unitGroup: String,
        include: String,
    ): CurrentConditionsDto {
        val url = buildWeatherUrl(
            weatherRequest,
            baseUrl,
            apiKey,
            unitGroup,
            include,
            startDate,
            endDate
        )

        return safeApiCall {
            val connection = openHttpsUrlConnection(url)
            val responseCode = connection.responseCode
            Log.d(TAG, "Response Code = $responseCode")

            handleHttpResponseCode(responseCode)

            val apiResponse = getApiResponseAsString(connection)
            val json = JSONObject(apiResponse).getJSONObject(JSON_ATTRIBUTE_TO_SELECT)
            val result: CurrentConditionsDto = parseCurrentConditions(json)

            connection to result  // return Pair(connection, result)
        }

    }

    private fun buildWeatherUrl(
        request: WeatherRequest,
        baseUrl: String,
        apiKey: String,
        unitGroup: String,
        include: String,
        startDate: String,
        endDate: String,
    ): String {
        return "$baseUrl/${request.latLong.latitude},${request.latLong.longitude}/${startDate}/${endDate}" +
                "?unitGroup=$unitGroup&lang=${request.language}&include=$include&key=$apiKey"
    }

    private fun openHttpsUrlConnection(urlString: String): HttpsURLConnection {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpsURLConnection
        connection.apply {
            requestMethod = HttpMethods.GET.toString()
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            connectTimeout = 10000
            readTimeout = 10000
            doInput = true
        }
        return connection
    }


    private inline fun <T> safeApiCall(requiredOperationsToGetDtoObject: () -> Pair<HttpsURLConnection, T>): T {
        var connection: HttpsURLConnection? = null
        return try {
            val (conn, dtoObject) = requiredOperationsToGetDtoObject()
            connection = conn
            dtoObject
        } catch (e: JSONException) {
            throw CustomException.DataException.ParsingException
        } catch (e: IOException) {
            throw CustomException.NetworkException.NoInternetConnection
        } catch (e: Exception) {
            throw CustomException.NetworkException.UnKnownNetworkException("Unknown error: ${e.message}")
        }finally {
            connection?.disconnect()
        }
    }

    private fun handleHttpResponseCode(code: Int) {
        when (code) {
            HttpsURLConnection.HTTP_OK -> return
            HttpsURLConnection.HTTP_UNAUTHORIZED -> throw CustomException.NetworkException.UnAuthorizedException
            HttpsURLConnection.HTTP_NOT_FOUND -> throw CustomException.NetworkException.NotFoundException
            HttpsURLConnection.HTTP_INTERNAL_ERROR -> throw CustomException.NetworkException.BadRequestException
            HttpsURLConnection.HTTP_BAD_REQUEST -> throw CustomException.NetworkException.BadRequestException
            429 -> throw CustomException.NetworkException.TooManyRequests
            else -> throw CustomException.NetworkException.UnKnownNetworkException(code.toString())
        }
    }

    private fun getApiResponseAsString(connection: HttpsURLConnection): String {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readText()
        reader.close()
        return response
    }

    private fun parseCurrentConditions(json: JSONObject): CurrentConditionsDto {
        return CurrentConditionsDto(
            cloudcover = json.getDouble("cloudcover"),
            conditions = json.getString("conditions"),
            datetime = json.getString("datetime"),
            datetimeEpoch = json.getInt("datetimeEpoch"),
            dew = json.getDouble("dew"),
            feelslike = json.getDouble("feelslike"),
            humidity = json.getDouble("humidity"),
            icon = json.getString("icon"),
            moonphase = json.getDouble("moonphase"),
            precip = json.opt("precip"),
            precipprob = json.getDouble("precipprob"),
            preciptype = json.opt("preciptype"),
            pressure = json.getDouble("pressure"),
            snow = json.getDouble("snow"),
            snowdepth = json.getDouble("snowdepth"),
            solarenergy = json.getDouble("solarenergy"),
            solarradiation = json.getDouble("solarradiation"),
            source = json.getString("source"),
            stations = json.getJSONArray("stations").let { arr ->
                List(arr.length()) { i -> arr.getString(i) }
            },
            sunrise = json.getString("sunrise"),
            sunriseEpoch = json.getInt("sunriseEpoch"),
            sunset = json.getString("sunset"),
            sunsetEpoch = json.getInt("sunsetEpoch"),
            temp = json.getDouble("temp"),
            uvindex = json.getDouble("uvindex"),
            visibility = json.getDouble("visibility"),
            winddir = json.getDouble("winddir"),
            windgust = json.opt("windgust"),
            windspeed = json.getDouble("windspeed")
        )
    }

    companion object {
        const val JSON_ATTRIBUTE_TO_SELECT = "currentConditions"

    }
}