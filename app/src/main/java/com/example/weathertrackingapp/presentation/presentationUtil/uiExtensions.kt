package com.example.weathertrackingapp.presentation.presentationUtil

import android.view.View
import android.widget.TextView

/**
 * Extension function for `TextView` to bind text or hide it based on the value.
 *
 * If the provided value is `null` or blank, the `TextView`'s visibility will be set to `INVISIBLE`.
 * If the value is not `null` or blank, it will set the `TextView`'s text to the value and make it visible.
 *
 * @param value The string value to bind to the `TextView`. If `null` or blank, the `TextView` will be hidden.
 */
fun TextView.bindOrHide(value: String?) {
    if (value.isNullOrBlank()) {
        visibility = View.INVISIBLE
    } else {
        text = value.toString()
        visibility = View.VISIBLE
    }
}