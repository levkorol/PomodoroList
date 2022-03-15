package ru.harlion.pomodorolist.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import ru.harlion.pomodorolist.R

class TimerService : Service() {

    private var timer: CountDownTimer? = null
    private var player: Player? = null

    inner class TimerBinder : Binder() {
        val service get() = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return TimerBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if(timer != null) {
            startService(Intent(this, TimerService::class.java))
        }
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notify: Notification = NotificationCompat.Builder(this, "channelId")
            .setContentTitle("Example Service")
            .setSmallIcon(R.drawable.ic_baseline_av_timer_24)
            .build()
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(1, notify)
        startForeground(1, notify)
        return super.onStartCommand(intent, flags, startId)
    }

    var onTick: ((millisUntilFinished: Long, timeFocus: Long) -> Unit)? = null
    var onFinish: (() -> Unit)? = null
    var isFocusTime = false
        private set

    fun startTimer(timeFocus: Long) {
        val prefs = Prefs(this)
        player = Player(this)

        timer = object : CountDownTimer(timeFocus, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                isFocusTime = true
                onTick?.invoke(millisUntilFinished, timeFocus)
                if (prefs.isSound) {
                    player?.playSound()
                }
            }

            override fun onFinish() {
                isFocusTime = false
                onFinish?.invoke()
                player?.stopSound()
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
        player?.stopSound()
        player = null
    }
}