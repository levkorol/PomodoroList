package ru.harlion.pomodorolist.ui.projects.adding

import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository
import ru.harlion.pomodorolist.models.Project
import ru.harlion.pomodorolist.models.Task

class AddProjectViewModel: ViewModel() {

    private val repository = Repository.get()

    fun addProject(
        name : String,
        tasks: List<Task>,
        prize: String,
        deadline: Long
    ) {
        val project = Project(
            name = name,
            tasks = tasks,
            prize = prize,
            deadline = deadline
        )

        repository.addProject(project)
    }
}