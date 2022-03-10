package ru.harlion.pomodorolist.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true)val id : Long = 0,
    var parentId: Long = 0,
    val parentColor: Int = 0,
    val name: String,
    val priority: String = "",
    var isDone: Boolean = false,
    val position: Int = 0,
    val countCyclePomodoro: Int = 0,
    val isPomodoro: Boolean = false,
    val date: Long = 0,
    val timeWork: Long = 0
    )