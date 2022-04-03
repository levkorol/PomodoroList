package ru.harlion.pomodorolist.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.ProjectWithProgress
import ru.harlion.pomodorolist.models.ProjectWithTasks
import ru.harlion.pomodorolist.models.Task

@Dao
abstract class ProjectDao {

    @Query("SELECT * ," +
            " (SELECT count(*) from task where parentId = project.id) as tasks," +
            " (SELECT count(*) from task where parentId = project.id and isDone = 1) as doneTasks" +
            " from project where isArchive = :isArchive")
    abstract fun progressTask(isArchive: Boolean) : LiveData<List<ProjectWithProgress>>

    @Query("SELECT * FROM project")
    abstract fun liveProjects(): LiveData<List<Project>>

    @Query("SELECT * FROM project WHERE id = (:id)")
    abstract fun liveProjectById(id: Long): LiveData<Project?>

    @Query("SELECT * FROM project WHERE id = (:id)")
    abstract fun projectById(id: Long): LiveData<ProjectWithTasks?>

    @Update
    abstract fun updateProject(project: Project)

    @Query("UPDATE project SET name = coalesce(:name, name), deadline = coalesce(:deadline, deadline), isArchive = coalesce(:isArchive, isArchive), prize = coalesce(:prize, prize) WHERE id = :projectId")
    abstract fun updateFieldsProject(projectId: Long, name: String? = null, deadline: Long? = null, isArchive: Boolean? = null, prize : String? = null)

    @Insert
    protected abstract fun addProject(project: Project): Long

    fun addProject(projectWithTasks: ProjectWithTasks): Long {
        val id = addProject(projectWithTasks.project)
        projectWithTasks.tasks.forEach {
            it.parentId = id
        }
        addTasks(projectWithTasks.tasks)
        return id
    }

    @Insert
    abstract fun addTasks(tasks: List<Task>)

    @Query("DELETE FROM project WHERE id = :projectId")
    abstract fun deleteProject(projectId: Long)
}


