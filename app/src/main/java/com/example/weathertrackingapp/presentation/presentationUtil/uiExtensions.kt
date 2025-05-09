package com.example.weathertrackingapp.presentation.presentationUtil

import android.view.View
import android.widget.TextView

fun TextView.bindOrHide(value: String?) {
    if (value.isNullOrBlank()) {
        visibility = View.INVISIBLE
    } else {
        text = value
        visibility = View.VISIBLE
    }
}