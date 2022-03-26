package ru.harlion.pomodorolist.ui.pomodoro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Task

class TimerViewModel : ViewModel() {

    private val repository = Repository.get()

    private var _task = MutableLiveData<Task?>()
    val task get(): LiveData<Task?> = _task

    fun getTaskById(id: Long) {
        _task.value = repository.getTaskById(id)
    }
}