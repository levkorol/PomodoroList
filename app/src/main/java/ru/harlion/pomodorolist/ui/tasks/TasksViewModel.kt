package ru.harlion.pomodorolist.ui.tasks

import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Task

class TasksViewModel: ViewModel() {

    private val repository = Repository.get()

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }
}