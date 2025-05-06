package com.example.weathertrackingapp.data.dataSources.remote.apiService

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import com.example.weathertrackingapp.common.customException.CustomException
import com.example.weathertrackingapp.data.dto.CurrentConditionsDto
import com.example.weathertrackingapp.data.dto.DayForecastDto
import com.example.weathertrackingapp.data.dto.FiveDaysForecastDto
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.domain.entity.responseEntities.FiveDaysForecast
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
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
        } catch (e: CustomException) {
            throw e
        } catch (e: Exception) {
            Log.d(TAG, "safeApiCall caught exception :$e ")
            throw CustomException.NetworkException.UnKnownNetworkException("Unknown error: ${e.message}")
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
                throw CustomException.NetworkException.UnKnownNetworkException(code.toString())
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
            CurrentConditionsDto::class -> buildCurrentConditionDtoObject(
                json.getJSONObject(ATTRIBUTE_TO_SELECT)
            ) as DTO

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
                    DayForecastDto(
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
            }
        )
    }

    private fun buildCurrentConditionDtoObject(json: JSONObject) = CurrentConditionsDto(
        cloudcover = json.getDouble("cloudcover"),
        conditions = json.getString("conditions"),
        datetime = json.getString("datetime"),
        datetimeEpoch = json.getInt("datetimeEpoch"),
        dew = json.getDouble("dew"),
        feelslike = json.getDouble("feelslike"),
        humidity = json.getDouble("humidity"),
        icon = json.getString("icon"),
        moonphase = json.getDouble("moonphase"),
        precipprob = json.getDouble("precipprob"),
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
        windspeed = json.getDouble("windspeed")
    )
}