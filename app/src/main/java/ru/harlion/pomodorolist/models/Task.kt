package ru.harlion.pomodorolist.models

data class Task (
    val id : Long,
    val parentId: Long,
    val parentColor: Int,
    val name: String,
    val priority: String,
    val isDone: Boolean,
    val position: Int,
    val countCyclePomodoro: Int,
    val isPomodoro: Boolean,
    val date: Long,
    val timeWork: Long
    )