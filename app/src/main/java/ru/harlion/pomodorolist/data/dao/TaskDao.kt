package ru.harlion.pomodorolist.data.dao


import androidx.lifecycle.LiveData
import androidx.room.*
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.models.TaskWithTime
import ru.harlion.pomodorolist.models.Time

@Dao
interface TaskDao {
    @Query("SELECT *, (select sum(time.focusTimeMills) from time where task.id = time.parentId)  as focusTimeMillis FROM task WHERE parentId = :projectId")
    fun getTasks(projectId : Long): List<TaskWithTime>

    @Query("SELECT * FROM task WHERE date BETWEEN :start AND :end")
    fun getTasksByTime(start: Long, end: Long) : List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    fun getTaskById(id: Long): Task?

    @Insert()
    fun trackTaskTime(time : Time)

    @Query("SELECT task.*, (select sum(time.focusTimeMills) from time where task.id = time.parentId)  as focusTimeMillis FROM task   where task.parentId = :parentId ")
    fun getTasksWithTime(parentId : Long) : LiveData<List<TaskWithTime>>

    @Update
    fun updateTask(task: Task)

    @Query("UPDATE project SET name = coalesce(:name, name), deadline = coalesce(:deadline, deadline), isArchive = coalesce(:isArchive, isArchive), prize = coalesce(:prize, prize) WHERE id = :projectId")
    fun updateTaskFields(projectId: Long, name: String? = null, deadline: Long? = null, isArchive: Boolean? = null, prize : String? = null)

    @Insert
    fun addTask(task: Task)

    @Query("DELETE FROM task WHERE id = :taskId")
    fun deleteTask(taskId: Long)



}