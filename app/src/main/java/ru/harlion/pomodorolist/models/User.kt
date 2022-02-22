package ru.harlion.pomodorolist.models

data class User (
    val id: Long,
    val name: String,
    val pomodoro: List<Pomodoro>
    )

data class Pomodoro (
    val id : Long,
    val dateStart: Long,
    val countPomodoro: Int,
    val time: Long
    )

fun loveFun(flowerA: Int, flowerB: Int): Boolean {
    return when  {
        flowerA % 2 == 0 && flowerB % 2 != 0 -> true
        flowerA % 2 != 0 && flowerB % 2 == 0 -> true
        else -> false
    }
}