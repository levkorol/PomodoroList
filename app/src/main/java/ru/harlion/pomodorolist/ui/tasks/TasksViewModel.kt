package ru.harlion.pomodorolist.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Task
import java.time.LocalDate

class TasksViewModel: ViewModel() {

    private val repository = Repository.get()

    val tasks = MutableLiveData<List<Task>>()

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun getTasksByDate(localDate: LocalDate) {
       tasks.value = repository.getTasksByDate(localDate)
    }

}