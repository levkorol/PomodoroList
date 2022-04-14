package ru.harlion.pomodorolist.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = ["id"],
        childColumns = ["parentId"]
    )]
)
class Time(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId : Long?,
    val focusTimeMills : Long,
    val epochDay : Long
)

data class TaskWithTime(
    @Embedded val task: Task,
    val focusTimeMillis : Long
)

data class ProjectWithTime(
    val name : String,
    val timeWork : Long
//    val color: Int
)