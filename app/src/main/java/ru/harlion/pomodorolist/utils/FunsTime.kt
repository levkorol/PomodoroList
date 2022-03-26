package ru.harlion.pomodorolist.utils

import android.content.res.Resources
import ru.harlion.pomodorolist.R
import java.text.SimpleDateFormat
import java.util.*

fun formatTimeHours(millis: Long, resources: Resources): String {
    val seconds = millis / 1000

    val minutes = seconds / 60
    val secsInMin = seconds % 60

    val hours = minutes / 60
    val minsInHour = minutes % 60

    return resources.getString(R.string.time_hours_minutes_seconds_formatter, hours, minsInHour, secsInMin)
}

fun formatTimeMins(millis: Long, resources: Resources): String {
    val seconds = millis / 1000

    val minutes = seconds / 60
    val secsInMin = seconds % 60

    val hours = minutes / 60
    val minsInHour = minutes % 60

    return resources.getString(R.string.time_minutes_seconds_formatter, minsInHour, secsInMin)
}

fun dateToString(date: Long): String {
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM ", Locale.getDefault())
    return dateFormat.format(date)
}

fun dateToStringShort(date: Long): String {
    val dateFormat = SimpleDateFormat(" EE, dd MMM", Locale.getDefault())
    return dateFormat.format(date)
}

fun timeToString(time: Long): String {
    val tim = SimpleDateFormat("HH:mm, EE", Locale.getDefault())
    return tim.format(time).toString()
}