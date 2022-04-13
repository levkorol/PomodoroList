package ru.harlion.pomodorolist.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
    entity = Project::class,
    parentColumns = ["id"],
    childColumns = ["parentId"],
    onDelete = CASCADE
)])
data class Task (
    @PrimaryKey(autoGenerate = true)val id : Long = 0,
    var parentId: Long = 0,
    var parentName: String = "",
    val parentColor: Int = 0,
    val name: String,
    val priority: String = "",
    var isDone: Boolean = false,
    val position: Int = 0,
    val date: Long = 0
    )