package ru.harlion.pomodorolist.models

import androidx.room.*

@Entity
class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var prize: String,
    val tasks: List<Task>,
    val isDone: Boolean = false,
    var deadline: Long = 0,
    val position: Int = 0,
    val color: Int = 0,
    val dateCreate: Long,
    val timeInFocus: Long = 0L,
    var isArchive: Boolean = false
)

class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentId"
    )
    val tasks: List<Task>
)