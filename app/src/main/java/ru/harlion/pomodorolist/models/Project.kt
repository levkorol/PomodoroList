package ru.harlion.pomodorolist.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val prize: String,
    val tasks: List<Task>,
    val isDone: Boolean = false,
    val deadline: Long = 0,
    val position: Int = 0,
   // val timeInFocus: Long
   // val dateCreate: Long
)

class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId"
    )
    val tasks: List<Task>
)