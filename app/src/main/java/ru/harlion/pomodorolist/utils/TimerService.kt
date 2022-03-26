package ru.harlion.pomodorolist.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.data.Repository

enum class TimerState {
    WAIT_FOCUS,
    FOCUS,
    PAUSE_FOCUS,
    WAIT_BREAK,
    BREAK
}

class TimerService : Service() {

    private val repository = Repository.get()
    private var timer: PausableCountDownTimer? = null
    private var player: Player? = null
    var taskId: Long = 0L
        private set
    var timerState = TimerState.WAIT_FOCUS
        private set(newState) {
            field = newState
            onStateChange?.invoke(newState)
        }

    inner class TimerBinder : Binder() {
        val service get() = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return TimerBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if (timer != null) {
            startService(Intent(this, TimerService::class.java))
        }
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notify: Notification = NotificationCompat.Builder(this, "channelId")
            .setContentTitle("Example Service")
            .setSmallIcon(R.drawable.ic_baseline_av_timer_24)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "channelId",
                    "channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        notificationManager.notify(1, notify)

        startForeground(1, notify)
        return super.onStartCommand(intent, flags, startId)
    }

    var onTick: ((millisUntilFinished: Long, timeFocus: Long) -> Unit)? = null
    var onFinish: (() -> Unit)? = null
    var onStateChange: ((TimerState) -> Unit)? = null

    fun startTimer(taskId: Long) {
        val prefs = Prefs(this)
        player = Player(this)
        this.taskId = taskId
        reallyStartTimer(prefs, taskId)
    }


    private fun reallyStartTimer(
        prefs: Prefs,
        taskId: Long
    ) {
        createAndStartTimer(
            prefs.focusTimerActiveSettings * 60000,
            TimerState.FOCUS,
            TimerState.WAIT_BREAK,
            prefs,
            taskId
        ) {
            if (prefs.isAutoBreakTimer) {
                createAndStartTimer(
                    prefs.breakTimerActiveSettings * 60000,
                    TimerState.BREAK,
                    TimerState.WAIT_FOCUS,
                    prefs,
                    taskId
                ) {
                    if (prefs.isAutoFocusTimer) {
                        reallyStartTimer(prefs, taskId)
                    }
                }
            }
        }
    }



    private fun createAndStartTimer(
        time: Long,
        tickState: TimerState,
        finishState: TimerState,
        prefs: Prefs,
        taskId: Long,
        andThen: (() -> Unit)? = null
    ) {
        timer = object : PausableCountDownTimer(time, 1000) {

            override fun onStop(elapsedMillis: Long) {
                taskId.let { repository.trackTimeTask(it, elapsedMillis) }
            }

            override fun onTick(millisUntilFinished: Long) {
                timerState = tickState
                onTick?.invoke(millisUntilFinished, time)
                if (prefs.isSound) {
                    player?.playSound()
                }
            }

            override fun onFinish() {
                timerState = finishState
                onFinish?.invoke()
                player?.stopSound()
                andThen?.invoke()
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
        player?.stopSound()
        player = null
        timerState = TimerState.WAIT_FOCUS
    }

    fun pause() {
        timer?.isPaused = true
        player?.stopSound()
        timerState = TimerState.PAUSE_FOCUS
    }

    fun resume(prefs: Prefs) {
        timerState = TimerState.FOCUS
        timer?.isPaused = false
        if (prefs.isSound) {
            player?.playSound()
        }
    }
}
