package com.example.weathertrackingapp.presentation.delegationPattern

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog

interface UiUtil {
    fun enableSwipeToRefreshFeature(
        rootLayout: ViewGroup,
        loadingProgressBar: ProgressBar,
        onRefresh: () -> Unit,
    )

    fun showDialog(
        context: Context,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String,
        onPositiveButtonClick: () -> Unit,
        onNegativeButtonClick: () -> Unit,
    ): AlertDialog
}