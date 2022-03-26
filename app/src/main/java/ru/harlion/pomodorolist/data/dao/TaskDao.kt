package ru.harlion.pomodorolist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE parentId = :projectId")
    fun getTasks(projectId : Long): List<Task>

    @Query("SELECT * FROM task WHERE date BETWEEN :start AND :end")
    fun getTasksByTime(start: Long, end: Long) : List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTaskById(id: Long): Task?

    @Query("UPDATE task SET timeWork = timeWork + :timeWork WHERE id = :id")
    fun trackTaskTime(id: Long, timeWork: Long)

    @Update
    fun updateTask(task: Task)

    @Insert
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM task")
    fun getTasks(): List<Task>

}