package ru.harlion.pomodorolist.ui.projects.adding


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