package ru.harlion.pomodorolist.ui.projects.detail_project


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.models.TaskWithTime

class DetailProjectViewModel : ViewModel() {

    private val repository = Repository.get()
    lateinit var project: LiveData<Project>
    lateinit var task: LiveData<List<TaskWithTime>>
    private var projectId = 0L

    fun getProjectById(id: Long) {
        project = repository.getProjectById(id)
        projectId = id
        task = repository.getTaskTime(projectId)
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun updateProjectName(name: String) {
        repository.updateNameProject(projectId, name)
    }

    fun updateArchive(isArchive: Boolean) {
        repository.updateIsArchiveProject(projectId, isArchive)
    }

    fun updateDeadline(deadline: Long) {
        repository.updateDeadlineProject(projectId, deadline)
    }

    fun updatePrize(prize: String) {
        repository.updatePrize(projectId, prize)
    }

    fun addTask(
        name: String,
        priorityTask: String,
        date: Long
    ) {
        val task = Task(
            name = name,
            priority = priorityTask,
            parentId = projectId,
            date = date
        )
        repository.addTask(task)
    }

    fun deleteProject() {
        repository.deleteProject(projectId)
    }
}