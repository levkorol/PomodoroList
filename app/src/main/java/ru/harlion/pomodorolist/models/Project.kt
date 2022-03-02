package ru.harlion.pomodorolist.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Project(
    @PrimaryKey(autoGenerate = true)val id: Long = -1,
    val name: String,
    val prize: String,
    val tasks: List<Task>,
    val isDone: Boolean = false,
    val deadline: Long = 0,
    val position: Int = 0,
   // val timeInFocus: Long
   // val dateCreate: Long
)