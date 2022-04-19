package ru.harlion.pomodorolist.ui.projects

import androidx.lifecycle.ViewModel
import ru.harlion.pomodorolist.data.Repository

class ProjectsVewModel : ViewModel() {
    private val repository = Repository.get()

    val projects =  repository.getListProjects()
}