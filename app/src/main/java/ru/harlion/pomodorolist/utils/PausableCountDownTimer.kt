package ru.harlion.pomodorolist.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock

abstract class PausableCountDownTimer(
    /**
     * Millis since epoch when alarm should stop.
     */
    mMillisInFuture: Long,
    /**
     * The interval in millis that the user receives callbacks
     */
    private var mCountdownInterval: Long
) {
    private var mStopTimeInFuture: Long = 0
    private var millisLeft: Long = mMillisInFuture

    var isPaused = false
    set(value) {
        if (field != value) {
            field = value
            if(value) {
                mHandler.removeMessages(MSG)
                millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime()
            } else {
                mStopTimeInFuture = SystemClock.elapsedRealtime() + millisLeft
                mHandler.sendMessage(mHandler.obtainMessage(MSG))
            }
        }
    }



    /**
     * boolean representing if the timer was cancelled
     */
    private var mCancelled = false

    /**
     * Cancel the countdown.
     */
    @Synchronized
    fun cancel() {
        mCancelled = true
        mHandler.removeMessages(MSG)
    }

    /**
     * Start the countdown.
     */
    @Synchronized
    fun start(): PausableCountDownTimer {
        mCancelled = false
        if (millisLeft <= 0) {
            onFinish()
            return this
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + millisLeft
        mHandler.sendMessage(mHandler.obtainMessage(MSG))
        return this
    }


    /**
     * Callback fired on regular interval.
     * @param millisUntilFinished The amount of time until finished.
     */
    abstract fun onTick(millisUntilFinished: Long)


    /**
     * Callback fired when the time is up.
     */
    abstract fun onFinish()

    // handles counting down
    private val mHandler: Handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            synchronized(this@PausableCountDownTimer) {
                if (mCancelled) {
                    return
                }
                val millisLeft =
                    mStopTimeInFuture - SystemClock.elapsedRealtime()
                if (millisLeft <= 0) {
                    onFinish()
                } else {
                    val lastTickStart = SystemClock.elapsedRealtime()
                    onTick(millisLeft)

                    // take into account user's onTick taking time to execute
                    val lastTickDuration =
                        SystemClock.elapsedRealtime() - lastTickStart
                    var delay: Long
                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) delay = 0
                    } else {
                        delay = mCountdownInterval - lastTickDuration

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval
                    }
                    sendMessageDelayed(obtainMessage(MSG), delay)
                }
            }
        }
    }

    companion object {
        private const val MSG = 1
    }
}
