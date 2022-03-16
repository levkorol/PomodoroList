package ru.harlion.pomodorolist.ui.projects.adding

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.base.MutableLiveEvent
import ru.harlion.pomodorolist.base.postContent
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.ProjectWithTasks
import ru.harlion.pomodorolist.models.Task

class AddProjectViewModel: ViewModel() {

    private val repository = Repository.get()
    val createdProjectId = MutableLiveEvent<Long>()
    val project = MutableLiveData<Project>()
    val tasks = MutableLiveData<List<Task>>()
    private var _projectId = -1L


    fun setProjectId(id : Long, load: Boolean) {
        if (_projectId != id ) {
            _projectId = id
            if(load) {
                repository.getProjectById(id) {
                    project.postValue(it?.project)
                    tasks.postValue(it?.tasks)
                }
            }
        }
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun addTask(
        name: String
    ) {
        val task = repository.addTask(Task(
            name = name,
            parentId = project.value?.id ?: 0L
        ))
    }

    fun addProject(
        name : String,
        tasks: List<Task>,
        prize: String,
        deadline: Long,
        color: Int
    ) {
        val project = ProjectWithTasks(Project(
            name = name,
            tasks = tasks,
            prize = prize,
            deadline = deadline,
            color = color,
            dateCreate = System.currentTimeMillis()
        ), tasks)

        repository.addProject(project) {
            createdProjectId.postContent(it)
        }
    }
}