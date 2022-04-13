package ru.harlion.pomodorolist.ui.tasks.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Task

class EditTaskViewModel : ViewModel() {

    private val repository = Repository.get()
    var task = MutableLiveData<Task?>()
    private var taskId = 0L

    fun getTaskById(id: Long) {
        task.value = repository.getTaskById(id)
        taskId = id
    }

    fun updateTask(
        name: String,
        priority: String,
        date : Long
    ) {
        val newTask = Task(
            id = task.value?.id ?: 0L,
            name = name,
            priority = priority,
            date = date,
            parentColor = task.value?.parentColor ?: 0,
            parentId = task.value?.parentId ?: -1L,
            parentName = task.value?.parentName ?: "",
            isDone = task.value?.isDone ?: false,
            position = task.value?.position ?: 0
        )
        repository.updateTask(newTask)
    }

    fun deleteTask() {
        repository.deleteTask(taskId)
    }
}