package ru.harlion.pomodorolist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.harlion.pomodorolist.models.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id = (:id)")
    fun getTaskById(id: Long): LiveData<Task?>

    @Update
    fun updateTask(task: Task)

    @Insert
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

}