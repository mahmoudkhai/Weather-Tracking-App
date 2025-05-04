package com.example.weathertrackingapp.presentation.presentationUtil

import android.content.Context
import androidx.appcompat.app.AlertDialog

interface UiUtil {
    fun showDialog(
        context: Context,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String,
        onPositiveButtonClick: () -> Unit,
        onNegativeButtonClick: () -> Unit,
    ):AlertDialog
}