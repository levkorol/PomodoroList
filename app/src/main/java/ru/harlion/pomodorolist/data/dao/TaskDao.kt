package ru.harlion.pomodorolist.data.dao


import androidx.room.*
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

    @Query("UPDATE project SET name = coalesce(:name, name), deadline = coalesce(:deadline, deadline), isArchive = coalesce(:isArchive, isArchive), prize = coalesce(:prize, prize) WHERE id = :projectId")
    abstract fun updateTaskFields(projectId: Long, name: String? = null, deadline: Long? = null, isArchive: Boolean? = null, prize : String? = null)

    @Insert
    fun addTask(task: Task)

    @Query("DELETE FROM task WHERE id = :taskId")
    fun deleteTask(taskId: Long)

    @Query("SELECT * FROM task")
    fun getTasks(): List<Task>

}