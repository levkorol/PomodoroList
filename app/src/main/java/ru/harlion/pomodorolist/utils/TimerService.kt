package ru.harlion.pomodorolist.utils

import android.app.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.utils.PausableCountDownTimer
import ru.harlion.pomodorolist.utils.Player
import ru.harlion.pomodorolist.utils.Prefs

enum class TimerState {
    WAIT_FOCUS,
    FOCUS,
    PAUSE_FOCUS,
    WAIT_BREAK,
    BREAK
}

class TimerService : Service() {

    private var notificationManager : NotificationManager?  = null

    private val repository = Repository.get()
    private var timer: PausableCountDownTimer? = null
        set(value) {
            if ((field == null) != (value == null)) {
                if (value == null) {
                    stopSelf()
                    stopForeground(true)
                    notificationManager = null
                }
                else startService(Intent(this, TimerService::class.java))
            }
            field = value
        }
    private var player: Player? = null
    var millisLeft = 0L
        private set

    var timerState = TimerState.WAIT_FOCUS
        private set(newState) {
            field = newState
            onStateChange?.invoke(newState)
            if(field != newState) {
                if(notificationManager != null) {
                    createNotification(notificationManager!!)
                }
            }
        }

    inner class TimerBinder : Binder() {
        val service get() = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder {
        return TimerBinder()
    }

    private fun setMessageNotification(): String {
        return when (timerState) {
            TimerState.FOCUS -> getString(R.string.now_in_focus)
            TimerState.WAIT_FOCUS -> getString(R.string.waiting_focus)
            TimerState.PAUSE_FOCUS -> getString(R.string.now_pause)
            TimerState.BREAK -> getString(R.string.now_break)
            TimerState.WAIT_BREAK -> getString(R.string.weiting_break)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager!!.createNotificationChannel(
                NotificationChannel(
                    "channelId",
                    "channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        val notify: Notification = createNotification(notificationManager!!)

        startForeground(1, notify)
        return super.onStartCommand(intent, flags, startId)
    }

    private val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        FLAG_UPDATE_CURRENT
    }

    private fun createNotification(notificationManager: NotificationManager): Notification {
        val notify: Notification = NotificationCompat.Builder(this, "channelId")
            .setContentTitle(setMessageNotification())
            .setSmallIcon(R.drawable.ic_baseline_av_timer_24)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(
                PendingIntent.getActivity(
                    this.baseContext, 0, Intent(this.baseContext, AppActivity::class.java), flags
                )
            )
            .build()
        notificationManager.notify(1, notify)
        return notify
    }

    var onTick: ((millisUntilFinished: Long, timeFocus: Long) -> Unit)? = null
    var onFinish: (() -> Unit)? = null
    var onStateChange: ((TimerState) -> Unit)? = null

    fun startTimer( isFocus: Boolean) {
        stopTimer()

        val prefs = Prefs(this)
        player = Player(this)
        val taskId = prefs.taskId

        if(isFocus) {
            startFocus(prefs, taskId)
        } else {
            startBreak(prefs, taskId)
        }
    }

    private fun startFocus(
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
                startBreak(prefs, taskId)
            }
        }
    }

    private fun startBreak(prefs: Prefs, taskId: Long) {
        createAndStartTimer(
            prefs.breakTimerActiveSettings * 60000,
            TimerState.BREAK,
            TimerState.WAIT_FOCUS,
            prefs,
            taskId
        ) {
            if (prefs.isAutoFocusTimer) {
                startFocus(prefs, taskId)
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
                millisLeft = millisUntilFinished
                if (prefs.songRawId > 0 && tickState == TimerState.FOCUS) {
                    player?.playSound()
                }
            }

            override fun onFinish(success: Boolean) {
                player?.stopSound()
                if (success) {
                    timerState = finishState
                    onFinish?.invoke()
                    andThen?.invoke()

                    if(prefs.signalRawId > 0 && tickState == TimerState.FOCUS) {
                        player?.playSignal()
                    }
                }

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
        if (prefs.songRawId > 0 ) {
            player?.playSound()
        }
    }
}
