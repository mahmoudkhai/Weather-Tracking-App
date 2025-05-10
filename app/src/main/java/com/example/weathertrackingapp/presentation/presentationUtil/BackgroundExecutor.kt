package com.example.weathertrackingapp.presentation.presentationUtil

import android.util.Log
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * A utility object that manages background task execution using a fixed thread pool.
 * This object allows you to run tasks on background threads, cancel the current task, and shut down the executor.
 *
 * It uses a thread pool with a fixed number of threads to efficiently manage background tasks.
 */
object BackgroundExecutor {
    private val executor = Executors.newFixedThreadPool(4)
    private var future: Future<*>? = null

    /**
     * Executes a given task on a background thread.
     *
     * @param task The task (a lambda function) to be executed on a background thread.
     */
    fun run(task: () -> Unit) {
        future = executor.submit(task)
    }

    /**
     * Shuts down the thread pool, terminating any active threads and preventing new tasks from being submitted.
     */
    fun shutdown() {
        Log.d(TAG, "shutDownAllThreads: called")
        executor.shutdown()
    }

    /**
     * Cancels the currently running task, if any.
     *
     * This method cancels the task that is currently executing in the background.
     */
    fun cancelCurrentWork() {
        Log.d(TAG, "cancelCurrentWork: called")
        future?.cancel(true)
    }
}