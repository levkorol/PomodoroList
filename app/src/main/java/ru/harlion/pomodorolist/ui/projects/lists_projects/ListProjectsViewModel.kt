package ru.harlion.pomodorolist.ui.projects.lists_projects


import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Task


class ListProjectsViewModel : ViewModel() {

    private val repository = Repository.get()

    val projects = repository.getListProjects()

    fun getListTasks(projectId: Long): List<Task> {
       return repository.getListTasksByProjectId(projectId)
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }
}