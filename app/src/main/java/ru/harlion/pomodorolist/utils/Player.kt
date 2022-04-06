package ru.harlion.pomodorolist.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class Player(val context: Context) {
    var mMediaPlayer: MediaPlayer? = null

    private lateinit var prefs: Prefs

    val handler = Handler(Looper.getMainLooper())

    val stopPlayer = Runnable {
        stopSound()
    }

    fun playSound() {
        if (mMediaPlayer == null) {
            prefs = Prefs(context)

            mMediaPlayer = MediaPlayer.create(context, prefs.songRawId)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    fun playSignal() {
        if (mMediaPlayer == null) {
            prefs = Prefs(context)

            mMediaPlayer = MediaPlayer.create(context, prefs.signalRawId)
            mMediaPlayer!!.isLooping = true

            handler.postDelayed(stopPlayer, 2000L)

            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    fun playSoundById(rawId : Int) {
        if (mMediaPlayer == null) {
            prefs = Prefs(context)

            mMediaPlayer = MediaPlayer.create(context, rawId)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    fun pauseSound() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) mMediaPlayer!!.pause()
    }

    fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        handler.removeCallbacks(stopPlayer)
    }
}