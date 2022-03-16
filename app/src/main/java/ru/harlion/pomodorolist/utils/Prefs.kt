package ru.harlion.pomodorolist.utils

import android.content.Context

class Prefs(val context: Context) {

    private val sharedPrefs = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    var isShowOnBoarding: Boolean
        get() = sharedPrefs.getBoolean("ON_BOARDING_SHOW", false)
        set(value) = sharedPrefs.edit().putBoolean("ON_BOARDING_SHOW", value)
            .apply()

    var focusTimerActiveSettings: Long
        get() = sharedPrefs.getLong("TIME_COUNT_MIN", 25)
        set(value) = sharedPrefs.edit().putLong("TIME_COUNT_MIN", value)
            .apply()


    var breakTimerActiveSettings: Long
        get() = sharedPrefs.getLong("BREAK_TIMER_MIN", 5)
        set(value) = sharedPrefs.edit().putLong("BREAK_TIMER_MIN", value)
            .apply()

    var song: Int
        get() = sharedPrefs.getInt("SOUND", -1)
        set(value) = sharedPrefs.edit().putInt("SOUND", value)
            .apply()

    var isSound: Boolean
        get() = sharedPrefs.getBoolean("IS_SOUND", false)
        set(value) = sharedPrefs.edit().putBoolean("IS_SOUND", value)
            .apply()

    var theme: String?
        get() = sharedPrefs.getString("THEME", "")
        set(value) = sharedPrefs.edit().putString("THEME", value)
            .apply()

}