package ru.harlion.pomodorolist.utils

import android.content.Context
import android.media.MediaPlayer

class Player(val context: Context) {
    var mMediaPlayer: MediaPlayer? = null

    private lateinit var prefs: Prefs

    fun playSound() {
        if (mMediaPlayer == null) {
            prefs = Prefs(context)

            mMediaPlayer = MediaPlayer.create(context, prefs.songRawId)//R.raw.alarm_clock
            mMediaPlayer!!.isLooping = true
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
    }
}