package ru.harlion.pomodorolist.ui.projects.detail_project


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task

class DetailProjectViewModel: ViewModel() {

    private val repository = Repository.get()
    var tasks = repository.getListTasks()
//    var project : LiveData<Project?>
//
//
//    init {
//        project = repository.getProject(project.value.id)
//    }

    fun addTask(
        name: String
    ) {
        val task = repository.addTask(Task(
          name = name
      ))
    }

}