package com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase

import android.view.View
import android.widget.TextView
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.common.di.AppDependenciesProvider
import com.example.weathertrackingapp.domain.entity.requestModels.LatLong
import com.example.weathertrackingapp.domain.entity.requestModels.WeatherRequest
import com.example.weathertrackingapp.presentation.WeatherApp
import com.example.weathertrackingapp.presentation.fragments.base.BaseFragment
import com.example.weathertrackingapp.presentation.model.FiveDaysForecast
import com.example.weathertrackingapp.presentation.presentationUtil.bindOrHide

class FiveDaysForecastFragment :
    BaseFragment<FiveDaysForecast>(R.layout.fragment_five_days_frocast) {

    override val viewModel by lazy {
        AppDependenciesProvider.provideFiveDaysForecastViewModel()
    }

    override fun initializeViews(view: View) {
        errorTextView = view.findViewById(R.id.errorTextView)
        loadingProgressBar = view.findViewById(R.id.progress_bar)
    }

    override fun registerObserverIntoViewModel() = viewModel.registerSubscriber(this)

    override fun createWeatherRequest(latLong: LatLong) =
        WeatherRequest(latLong = latLong, language = WeatherApp.systemLanguage)

    override fun getDataInBackgroundThread(weatherRequest: WeatherRequest) =
        Thread {
            viewModel.processUserIntent(
                FiveDaysForecastScreenContract.Intent.GetFiveDaysForecast(
                    weatherRequest
                )
            )
        }.start()


    override fun bindViews(fiveDaysForecast: FiveDaysForecast) {
        val view = requireView()
        val firstDay = fiveDaysForecast.days?.get(0)
        val secondDay = fiveDaysForecast.days?.get(1)
        val thirdDay = fiveDaysForecast.days?.get(2)
        val fourthDay = fiveDaysForecast.days?.get(3)
        val fifthDay = fiveDaysForecast.days?.get(4)
        view.apply {
            findViewById<TextView>(R.id.TVfirstDayResolvedAddress).bindOrHide(fiveDaysForecast.resolvedAddress)
            findViewById<TextView>(R.id.TVfirstDayAddress).bindOrHide(fiveDaysForecast.address)
            findViewById<TextView>(R.id.TVfirstDayDescription).bindOrHide(firstDay?.description)
            findViewById<TextView>(R.id.TVfirstDayConditions).bindOrHide(firstDay?.conditions)
            findViewById<TextView>(R.id.TVfirstDayDateTime).bindOrHide(firstDay?.datetime)
            findViewById<TextView>(R.id.TVfirstDayIcon).bindOrHide(firstDay?.icon)
            findViewById<TextView>(R.id.TVfirstDayTemp).bindOrHide(firstDay?.temp)
            findViewById<TextView>(R.id.TVfirstDayWindSpeed).bindOrHide(firstDay?.windspeed)

            // Second day bindings
            findViewById<TextView>(R.id.TVDescription2).bindOrHide(secondDay?.description)
            findViewById<TextView>(R.id.TVConditions2).bindOrHide(secondDay?.conditions)
            findViewById<TextView>(R.id.TVDateTime2).bindOrHide(secondDay?.datetime)
            findViewById<TextView>(R.id.TVIcon2).bindOrHide(secondDay?.icon)
            findViewById<TextView>(R.id.TVTemp2).bindOrHide(secondDay?.temp)
            findViewById<TextView>(R.id.TVWindSpeed2).bindOrHide(secondDay?.windspeed)

            // Third day bindings
            findViewById<TextView>(R.id.TVDescription3).bindOrHide(thirdDay?.description)
            findViewById<TextView>(R.id.TVConditions3).bindOrHide(thirdDay?.conditions)
            findViewById<TextView>(R.id.TVDateTime3).bindOrHide(thirdDay?.datetime)
            findViewById<TextView>(R.id.TVIcon3).bindOrHide(thirdDay?.icon)
            findViewById<TextView>(R.id.TVTemp3).bindOrHide(thirdDay?.temp)
            findViewById<TextView>(R.id.TVWindSpeed3).bindOrHide(thirdDay?.windspeed)

            // Fourth day bindings
            findViewById<TextView>(R.id.TVDescription4).bindOrHide(fourthDay?.description)
            findViewById<TextView>(R.id.TVConditions4).bindOrHide(fourthDay?.conditions)
            findViewById<TextView>(R.id.TVDateTime4).bindOrHide(fourthDay?.datetime)
            findViewById<TextView>(R.id.TVIcon4).bindOrHide(fourthDay?.icon)
            findViewById<TextView>(R.id.TVTemp4).bindOrHide(fourthDay?.temp)
            findViewById<TextView>(R.id.TVWindSpeed4).bindOrHide(fourthDay?.windspeed)

            // Fifth day bindings
            findViewById<TextView>(R.id.TVDescription5).bindOrHide(fifthDay?.description)
            findViewById<TextView>(R.id.TVConditions5).bindOrHide(fifthDay?.conditions)
            findViewById<TextView>(R.id.TVDateTime5).bindOrHide(fifthDay?.datetime)
            findViewById<TextView>(R.id.TVIcon5).bindOrHide(fifthDay?.icon)
            findViewById<TextView>(R.id.TVTemp5).bindOrHide(fifthDay?.temp)
            findViewById<TextView>(R.id.TVWindSpeed5).bindOrHide(fifthDay?.windspeed)

        }
    }

    override fun unRegisterFragmentFromViewModel() = viewModel.unregisterSubscriber(this)
}