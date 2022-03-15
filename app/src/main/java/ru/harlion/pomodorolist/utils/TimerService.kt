package ru.harlion.pomodorolist.utils

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder

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
       ( getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(1, TODO())
        startForeground(1, TODO())
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