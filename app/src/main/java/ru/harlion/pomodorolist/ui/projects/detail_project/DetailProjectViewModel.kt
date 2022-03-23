package ru.harlion.pomodorolist.ui.projects.detail_project


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.ProjectWithTasks
import ru.harlion.pomodorolist.models.Task

class DetailProjectViewModel : ViewModel() {

    private val repository = Repository.get()
    lateinit var projectWithTasks: LiveData<ProjectWithTasks?>
    private var projectId = 0L

    fun getProjectById(id: Long) {
        projectWithTasks = repository.getProjectById(id)
        projectId = id
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun updateProjectName(
        name: String
    ) {
        repository.updateNameProject(projectId, name)
    }

    fun updateArchive(isArchive: Boolean) {
        projectWithTasks.value?.project?.let {
            it.isArchive = isArchive
            repository.updateProject(it)
        }
    }

    fun addTask(
        name: String,
        priorityTask: String,
        date: Long
    ) {
        val task = Task(
            name = name,
            priority = priorityTask,
            parentId = projectWithTasks.value?.project?.id ?: 0L,
            date = date
        )

        repository.addTask(task)
    }

    fun deleteProject() {
        projectWithTasks.value?.project?.let { repository.deleteProject(it) }
    }
}