package ru.harlion.pomodorolist.ui.projects.detail_project


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task

class DetailProjectViewModel: ViewModel() {

    private val repository = Repository.get()
    val project = MutableLiveData<Project>()
    val tasks = MutableLiveData<List<Task>>()


    fun getProjectById(id: Long) {
        repository.getProjectById(id) {
            project.postValue(it?.project)
            tasks.postValue(it?.tasks)
        }
    }

    fun addTask(
        name: String
    ) {
        val task = repository.addTask(Task(
          name = name,
          parentId = project.value?.id ?: 0L
      ))
    }

}