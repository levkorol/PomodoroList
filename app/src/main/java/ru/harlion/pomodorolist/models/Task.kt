package ru.harlion.pomodorolist.models

data class Task (
    val id : Long,
    val parentId: Long,
    val name: String,
    val isDone: Boolean,
    val position: Int,

        )