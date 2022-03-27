package ru.harlion.pomodorolist.ui.tasks.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Task

class EditTaskViewModel : ViewModel() {

    private val repository = Repository.get()
    var task = MutableLiveData<Task?>()

    fun getTaskById(id: Long) {
        task.value = repository.getTaskById(id)
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }
}