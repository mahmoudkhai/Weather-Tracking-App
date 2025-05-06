package com.example.weathertrackingapp.presentation.fragments.fiveDaysForecase

import android.os.Bundle
import android.view.View
import com.example.weathertrackingapp.R
import com.example.weathertrackingapp.domain.model.responseModels.FiveDaysForecast
import com.example.weathertrackingapp.presentation.fragments.BaseFragment

class FiveDaysForecastFragment : BaseFragment<FiveDaysForecast>(R.layout.fragment_five_days_frocast) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun showError(errorMessage: String) {
        TODO("Not yet implemented")
    }

    override fun bindViews(data: FiveDaysForecast) {
        TODO("Not yet implemented")
    }


}