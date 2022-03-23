package ru.harlion.pomodorolist.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.ProjectWithTasks
import ru.harlion.pomodorolist.models.Task
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.Executors


private const val DATABASE_NAME = "pomodoro-database"

class Repository private constructor(context: Context) {


    private val database: DataBaseApp = Room.databaseBuilder(
        context.applicationContext,
        DataBaseApp::class.java,
        DATABASE_NAME
    ).allowMainThreadQueries()
        .build()

    private val projectDao = database.projectDao()
    private val taskDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun addProject(projectWithTasks: ProjectWithTasks, code: (Long) -> Unit) {
        executor.execute {
            val projectId = projectDao.addProject(projectWithTasks)
            code.invoke(projectId)
        }
    }

    fun getProjectById(projectId: Long) = projectDao.projectById(projectId)

    fun addTask(task: Task) {
        executor.execute {
            taskDao.addTask(task)
        }
    }

    fun getListProjects(): LiveData<List<Project>> {
        return projectDao.liveProjects()
    }

    fun getListTasksByProjectId(projectId: Long): List<Task> {
        return taskDao.getTasks(projectId = projectId)
    }

    fun getTasksByDate(localDate: LocalDate): List<Task> {
        return taskDao.getTasksByTime(
            localDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000,
            localDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000 - 1
        )
    }

    fun updateTask(task: Task) {
        return taskDao.updateTask(task)
    }

    fun deleteProject(project: Project) = projectDao.deleteProject(project)

    fun updateProject(project: Project) = projectDao.updateProject(project)

    fun updateNameProject(projectId: Long, name: String) {
        projectDao.updateName(projectId, name)
    }

    fun getProject(id: Long): LiveData<Project?> {
        return projectDao.liveProjectById(id)
    }

    companion object {
        private var INSTANCE: Repository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Repository(context)
            }
        }

        fun get(): Repository {
            return INSTANCE ?: throw IllegalStateException("Repository must be init")
        }

    }
}