package com.example.weathertrackingapp.presentation.presentationUtil

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import java.util.concurrent.Executors
import java.util.concurrent.Future

object BackgroundExecutor {
    // Create a fixed-size thread pool with 4 threads (you can adjust the size)
    private val executor = Executors.newFixedThreadPool(4)
    private var future: Future<*>? = null

    fun run(task: () -> Unit) {
        future = executor.submit(task)
    }

    fun shutdown() {
        Log.d(TAG, "shutDownAllThreads: called")
        executor.shutdown()
    }

    fun cancelCurrentWork() {
        Log.d(TAG, "cancelCurrentWork: called")
        future?.cancel(true)
    }
}