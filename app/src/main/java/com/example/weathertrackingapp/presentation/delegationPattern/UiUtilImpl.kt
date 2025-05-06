package com.example.weathertrackingapp.presentation.delegationPattern

import android.content.Context
import androidx.appcompat.app.AlertDialog

class UiUtilImpl : UiUtil {
    override fun showDialog(
        context: Context,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String,
        onPositiveButtonClick: () -> Unit,
        onNegativeButtonClick: () -> Unit,
    ) = AlertDialog.Builder(context).setTitle(title).setMessage(message)
        .setPositiveButton(positiveButton) { dialog, _ ->
            onPositiveButtonClick()
            dialog.dismiss()
        }.setNegativeButton(negativeButton) { dialog, _ ->
            onNegativeButtonClick()
            dialog.dismiss()
        }.create().apply { show() }
}