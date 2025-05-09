package com.example.weathertrackingapp.data.repository.dataSources.remote.apiService

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.data.dto.CurrentConditionDto
import com.example.weathertrackingapp.data.dto.CurrentWeatherDto
import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.data.dto.WholeDayWeatherDto
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.reflect.KClass

class ApiServiceImpl : ApiService {

    override fun <ResponseType : Any> get(
        weatherRequest: WeatherRequest,
        responseType: KClass<ResponseType>,
        baseUrl: String,
        startDate: String,
        endDate: String,
        apiKey: String,
        unitGroup: String,
        include: String,
    ): ResponseType {
        val url = buildWeatherUrl(
            weatherRequest, baseUrl, apiKey, unitGroup, include, startDate, endDate
        )

        return apiCall {
            val connection = openHttpsUrlConnection(url)
            val responseCode = connection.responseCode
            Log.d(TAG, "Response Code = $responseCode")

            handleHttpResponseCode(responseCode)

            val apiResponse: String = getApiResponseAsString(connection)
            Log.d(TAG, "API response = $apiResponse")
            val json: JSONObject = JSONObject(apiResponse)
            Log.d(TAG, "Json Object = $json")
            val result = getDtoModelFromJson<ResponseType>(json, responseType = responseType)
            Log.d(TAG, "dto after mapping = $result")
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
        return "$baseUrl/${request.latLong.latitude},${request.latLong.longitude}/${startDate}/${endDate}" + "?unitGroup=$unitGroup&lang=${request.language}&include=$include&key=$apiKey"
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


    private inline fun <T> apiCall(requiredOperationsToGetDtoObject: () -> Pair<HttpsURLConnection, T>): T {
        var connection: HttpsURLConnection? = null
        return try {
            val (conn: HttpsURLConnection, dtoObject) = requiredOperationsToGetDtoObject()
            connection = conn
            dtoObject
        } finally {
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
            else -> {
                Log.d(TAG, "handleHttpResponseCode throws unKnownNetworkException with code:$code ")
                throw CustomException.NetworkException.UnKnownNetworkException("Un Known Network Error with Code $code")
            }
        }
    }

    private fun getApiResponseAsString(connection: HttpsURLConnection): String {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val response = reader.readText()
        reader.close()
        return response
    }

    @Suppress("UNCHECKED_CAST")
    private fun <DTO : Any> getDtoModelFromJson(
        json: JSONObject,
        responseType: KClass<DTO>,
    ): DTO {
        return when (responseType) {
            CurrentWeatherDto::class -> buildCurrentWeatherDtoObject(json) as DTO

            FiveDaysForecastDto::class -> buildFiveDaysForecastDtoObject(json) as DTO
            else -> throw CustomException.DataException.UnSupportedTypeCasting
        }
    }

    companion object {
        const val ATTRIBUTE_TO_SELECT = "currentConditions"

    }

    private fun buildFiveDaysForecastDtoObject(json: JSONObject): FiveDaysForecastDto {
        return FiveDaysForecastDto(
            resolvedAddress = json.getString("resolvedAddress"),
            address = json.getString("address"),
            timezone = json.getString("timezone"),
            days = json.getJSONArray("days").let { daysArray ->
                List(daysArray.length()) { i ->
                    val dayJson = daysArray.getJSONObject(i)
                    WholeDayWeatherDto(
                        cloudcover = dayJson.getDouble("cloudcover"),
                        conditions = dayJson.getString("conditions"),
                        datetime = dayJson.getString("datetime"),
                        datetimeEpoch = dayJson.getInt("datetimeEpoch"),
                        description = dayJson.getString("description"),
                        dew = dayJson.getDouble("dew"),
                        feelslike = dayJson.getDouble("feelslike"),
                        feelslikemax = dayJson.getDouble("feelslikemax"),
                        feelslikemin = dayJson.getDouble("feelslikemin"),
                        humidity = dayJson.getDouble("humidity"),
                        icon = dayJson.getString("icon"),
                        moonphase = dayJson.getDouble("moonphase"),
                        precip = dayJson.getDouble("precip"),
                        precipcover = dayJson.getDouble("precipcover"),
                        precipprob = dayJson.getDouble("precipprob"),
                        pressure = dayJson.getDouble("pressure"),
                        severerisk = dayJson.getDouble("severerisk"),
                        snow = dayJson.getDouble("snow"),
                        snowdepth = dayJson.getDouble("snowdepth"),
                        solarenergy = dayJson.getDouble("solarenergy"),
                        solarradiation = dayJson.getDouble("solarradiation"),
                        source = dayJson.getString("source"),
                        sunrise = dayJson.getString("sunrise"),
                        sunriseEpoch = dayJson.getInt("sunriseEpoch"),
                        sunset = dayJson.getString("sunset"),
                        sunsetEpoch = dayJson.getInt("sunsetEpoch"),
                        temp = dayJson.getDouble("temp"),
                        tempmax = dayJson.getDouble("tempmax"),
                        tempmin = dayJson.getDouble("tempmin"),
                        uvindex = dayJson.getDouble("uvindex"),
                        visibility = dayJson.getDouble("visibility"),
                        winddir = dayJson.getDouble("winddir"),
                        windgust = dayJson.getDouble("windgust"),
                        windspeed = dayJson.getDouble("windspeed")
                    )
                }
            })
    }

    private fun buildCurrentWeatherDtoObject(json: JSONObject): CurrentWeatherDto {
        return CurrentWeatherDto(
            queryCost = json.optInt("queryCost"),
            resolvedAddress = json.optString("resolvedAddress"),
            timeZone = json.optString("timezone"),
            address = json.optString("address"),
            currentConditions = json.optJSONObject("currentConditions")?.let { conditionJson ->
                CurrentConditionDto(
                    cloudcover = conditionJson.optDouble("cloudcover"),
                    conditions = conditionJson.optString("conditions"),
                    datetime = conditionJson.optString("datetime"),
                    datetimeEpoch = conditionJson.optInt("datetimeEpoch"),
                    dew = conditionJson.optDouble("dew"),
                    feelslike = conditionJson.optDouble("feelslike"),
                    humidity = conditionJson.optDouble("humidity"),
                    icon = conditionJson.optString("icon"),
                    moonphase = conditionJson.optDouble("moonphase"),
                    precipprob = conditionJson.optDouble("precipprob"),
                    pressure = conditionJson.optDouble("pressure"),
                    snow = conditionJson.optDouble("snow"),
                    snowdepth = conditionJson.optDouble("snowdepth"),
                    solarenergy = conditionJson.optDouble("solarenergy"),
                    solarradiation = conditionJson.optDouble("solarradiation"),
                    source = conditionJson.optString("source"),
                    sunrise = conditionJson.optString("sunrise"),
                    sunriseEpoch = conditionJson.optInt("sunriseEpoch"),
                    sunset = conditionJson.optString("sunset"),
                    sunsetEpoch = conditionJson.optInt("sunsetEpoch"),
                    temp = conditionJson.optDouble("temp"),
                    uvindex = conditionJson.optDouble("uvindex"),
                    visibility = conditionJson.optDouble("visibility"),
                    winddir = conditionJson.optDouble("winddir"),
                    windspeed = conditionJson.optDouble("windspeed")
                )
            },
            days = json.getJSONArray("days").getJSONObject(0)?.let { dayJson ->
                WholeDayWeatherDto(
                    cloudcover = dayJson.getDouble("cloudcover"),
                    conditions = dayJson.getString("conditions"),
                    datetime = dayJson.getString("datetime"),
                    datetimeEpoch = dayJson.getInt("datetimeEpoch"),
                    description = dayJson.getString("description"),
                    dew = dayJson.getDouble("dew"),
                    feelslike = dayJson.getDouble("feelslike"),
                    feelslikemax = dayJson.getDouble("feelslikemax"),
                    feelslikemin = dayJson.getDouble("feelslikemin"),
                    humidity = dayJson.getDouble("humidity"),
                    icon = dayJson.getString("icon"),
                    moonphase = dayJson.getDouble("moonphase"),
                    precip = dayJson.getDouble("precip"),
                    precipcover = dayJson.getDouble("precipcover"),
                    precipprob = dayJson.getDouble("precipprob"),
                    pressure = dayJson.getDouble("pressure"),
                    severerisk = dayJson.getDouble("severerisk"),
                    snow = dayJson.getDouble("snow"),
                    snowdepth = dayJson.getDouble("snowdepth"),
                    solarenergy = dayJson.getDouble("solarenergy"),
                    solarradiation = dayJson.getDouble("solarradiation"),
                    source = dayJson.getString("source"),
                    sunrise = dayJson.getString("sunrise"),
                    sunriseEpoch = dayJson.getInt("sunriseEpoch"),
                    sunset = dayJson.getString("sunset"),
                    sunsetEpoch = dayJson.getInt("sunsetEpoch"),
                    temp = dayJson.getDouble("temp"),
                    tempmax = dayJson.getDouble("tempmax"),
                    tempmin = dayJson.getDouble("tempmin"),
                    uvindex = dayJson.getDouble("uvindex"),
                    visibility = dayJson.getDouble("visibility"),
                    winddir = dayJson.getDouble("winddir"),
                    windgust = dayJson.getDouble("windgust"),
                    windspeed = dayJson.getDouble("windspeed")
                )
            })
    }
}
