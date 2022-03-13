package ru.harlion.pomodorolist.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Task

class TasksViewModel: ViewModel() {

    private val repository = Repository.get()

    val tasks = MutableLiveData<List<Task>>()

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun getTasksByDate() {
       tasks.value = repository.getTasksByDate()
    }
}