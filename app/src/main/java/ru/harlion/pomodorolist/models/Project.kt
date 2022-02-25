package ru.harlion.pomodorolist.models


class Project(
    val id: Long,
    val name: String,
    val prize: String,
    val tasks: List<Task>,
    val isDone: Boolean,
    val deadline: Long = 0,
    val position: Int,
   // val timeInFocus: Long
)