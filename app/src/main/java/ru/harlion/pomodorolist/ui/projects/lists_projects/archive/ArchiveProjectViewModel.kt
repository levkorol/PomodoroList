package ru.harlion.pomodorolist.ui.projects.lists_projects.archive

import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Task
import ru.harlion.pomodorolist.models.TaskWithTime

class ArchiveProjectViewModel: ViewModel() {

    private val repository = Repository.get()

    val project =  repository.progressTask(true)

    fun getListTasks(projectId: Long): List<TaskWithTime> {
        return repository.getListTasksByProjectId(projectId)
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }
}