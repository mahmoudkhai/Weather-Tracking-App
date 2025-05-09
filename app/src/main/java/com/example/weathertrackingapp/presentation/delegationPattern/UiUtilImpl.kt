package com.example.weathertrackingapp.presentation.delegationPattern

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.example.weathertrackingapp.common.constants.CommonConstants.TAG

class UiUtilImpl : UiUtil {

    override fun enableSwipeToRefreshFeature(
        rootLayout: ViewGroup,
        loadingProgressBar: ProgressBar,
        onRefresh: () -> Unit,
    ) {
        setupSwipeListener(rootLayout, loadingProgressBar, onRefresh = onRefresh)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSwipeListener(
        rootLayout: ViewGroup,
        loadingProgressBar: ProgressBar,
        onRefresh: () -> Unit,
    ) {
        var isThresholdReached = false

        var longPressDetected = false
        val handler = Handler(Looper.getMainLooper())
        val longPressRunnable = Runnable {
            longPressDetected = true
            // Handle long press logic here
        }

        rootLayout.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    longPressDetected = false
                    handler.postDelayed(longPressRunnable, 500) // 500ms delay for long press
                    firstTouchPointOnVerticalAxis = motionEvent.y // first touch point
                    CONTINUE_LISTENING_FOR_USER_TOUCH
                }

                MotionEvent.ACTION_MOVE -> {
                    if (longPressDetected) resetProgressBar(loadingProgressBar)
                    if (userSwappedDown(motionEvent) && !isThresholdReached) {  // only respond to downward swipes
                        showProgressBarWhileUserSwipingDown(loadingProgressBar, motionEvent)
                    }
                    if (userReachedThresholdPoint(motionEvent) && !isThresholdReached) {
                        isThresholdReached = true
                        triggerRefresh(rootLayout, loadingProgressBar, onRefresh)
                        return@setOnTouchListener STOP_LISTENING_FOR_USER_TOUCH
                    } else CONTINUE_LISTENING_FOR_USER_TOUCH
                }

                MotionEvent.ACTION_UP -> {
                    resetProgressBar(loadingProgressBar)
                    isThresholdReached = false
                    STOP_LISTENING_FOR_USER_TOUCH
                }

                else -> {
                    isThresholdReached = true
                    STOP_LISTENING_FOR_USER_TOUCH
                }
            }
        }
    }

    private fun hideProgressBar(loadingProgressBar: ProgressBar) {
        loadingProgressBar.apply {
            visibility = View.GONE
            isIndeterminate = false
            translationY = 0f
        }
    }

    private fun showProgressBarWhileUserSwipingDown(
        loadingProgressBar: ProgressBar,
        motionEvent: MotionEvent,
    ) {
        loadingProgressBar.apply {
            isIndeterminate = false
            visibility = View.VISIBLE
            loadingProgressBar.translationY =
                getHowFarUserSwipedDownward(motionEvent) / 2  // divide to make it smoother
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun triggerRefresh(
        frameLayout: ViewGroup,
        loadingProgressBar: ProgressBar,
        onRefresh: () -> Unit,
    ) {
        //Once the animation finishes, the progress bar stays at that final position indefinitely — it does not return back automatically
        animateProgressBarVertically(loadingProgressBar)

        Log.d(TAG, "triggerRefresh: called")
        // Show progress bar when refresh is triggered
        frameLayout.setOnTouchListener(null) // Disable swipe gesture while refreshing

        onRefresh()  // This is where the child fragment’s refresh logic will be executed
        resetProgressBar(loadingProgressBar)
        setupSwipeListener(
            frameLayout,
            loadingProgressBar,
            onRefresh
        )  // Re-enable swipe gesture after refresh
    }

    private fun animateProgressBarVertically(loadingProgressBar: ProgressBar) =
        loadingProgressBar.apply {
            animate()
                .translationY(PROGRESS_BAR_SHIFTING_FROM_ORIGINAL_POSITION)  //For example, if CENTER_OF_SCREEN is 300f, it moves the view 300 pixels down.
                .setDuration(SHIFTING_DURATION_IN_MS)
                .start()
        }

    private fun resetProgressBar(loadingProgressBar: ProgressBar) = loadingProgressBar.animate()
        .translationY(BASE_PROGRESS_BAR_POSITION)
        .setDuration(SHIFTING_DURATION_IN_MS)
        .withEndAction {
            loadingProgressBar.visibility = View.GONE
        }
        .start()


    private fun userReachedThresholdPoint(motionEvent: MotionEvent) =
        getHowFarUserSwipedDownward(motionEvent) > USER_SWIPE_THRESHOLD


    private fun userSwappedDown(motionEvent: MotionEvent) =
        getHowFarUserSwipedDownward(motionEvent) > TOP_OF_SCREEN

    private fun getHowFarUserSwipedDownward(motionEvent: MotionEvent) =
        motionEvent.y - firstTouchPointOnVerticalAxis

    companion object {
        private const val CONTINUE_LISTENING_FOR_USER_TOUCH = true
        private const val STOP_LISTENING_FOR_USER_TOUCH = false
        private var firstTouchPointOnVerticalAxis = 0f
        private const val USER_SWIPE_THRESHOLD = 1200f
        private const val TOP_OF_SCREEN = 0f
        private const val PROGRESS_BAR_SHIFTING_FROM_ORIGINAL_POSITION = 0f
        private const val SHIFTING_DURATION_IN_MS = 500L
        private const val BASE_PROGRESS_BAR_POSITION = 0f
    }

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