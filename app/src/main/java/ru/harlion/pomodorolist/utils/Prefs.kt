package ru.harlion.pomodorolist.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData

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

    var theme: String?
        get() = sharedPrefs.getString("THEME", "")
        set(value) = sharedPrefs.edit().putString("THEME", value)
            .apply()

    var isAutoFocusTimer: Boolean
        get() = sharedPrefs.getBoolean("AUTO_FOCUS", false)
        set(value) = sharedPrefs.edit().putBoolean("AUTO_FOCUS", value)
            .apply()

    var isAutoBreakTimer: Boolean
        get() = sharedPrefs.getBoolean("AUTO_Break", false)
        set(value) = sharedPrefs.edit().putBoolean("AUTO_Break", value)
            .apply()

    var taskId: Long
        get() = sharedPrefs.getLong("TASK_ID", 0)
        set(value) = sharedPrefs.edit().putLong("TASK_ID", value)
            .apply()

    fun observeTaskId() : LiveData<Long> =
        object : LiveData<Long>(), SharedPreferences.OnSharedPreferenceChangeListener {
            init {
                value = taskId
                sharedPrefs.registerOnSharedPreferenceChangeListener(this)
            }

            override fun onSharedPreferenceChanged(
                sharedPreferences: SharedPreferences?,
                key: String?
            ) {
                if(key == "TASK_ID") {
                    postValue(taskId)
                }
            }
        }
}