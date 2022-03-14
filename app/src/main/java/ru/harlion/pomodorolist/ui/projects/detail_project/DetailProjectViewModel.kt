package ru.harlion.pomodorolist.ui.projects.detail_project


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task

class DetailProjectViewModel : ViewModel() {

    private val repository = Repository.get()
    val project = MutableLiveData<Project>()
    val tasks = MutableLiveData<List<Task>>()


    fun getProjectById(id: Long) {
        repository.getProjectById(id) {
            project.postValue(it?.project)
            tasks.postValue(it?.tasks)
        }
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun updateProject(
        name: String
    ) {
        project.value?.name = name
        repository.updateProject(project.value!!)
    }

    fun addTask(
        name: String,
        priorityTask : String
    ) {
        val task = Task(
            name = name,
            priority = priorityTask,
            parentId = project.value?.id ?: 0L,
            date = System.currentTimeMillis()
        )

        repository.addTask(task)

        tasks.value = (tasks.value ?: emptyList()) + task
    }

    fun deleteProject() {
        repository.deleteProject(project.value!!)
    }

}