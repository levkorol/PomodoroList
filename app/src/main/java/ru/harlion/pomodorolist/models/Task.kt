package ru.harlion.pomodorolist.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (
    @PrimaryKey(autoGenerate = true)val id : Long,
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