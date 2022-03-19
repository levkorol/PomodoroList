package ru.harlion.pomodorolist.ui.profile.archive


import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentArchiveProjectBinding
import ru.harlion.pomodorolist.ui.projects.adapter.ProjectsAdapter


class ArchiveProjectFragment :
    BindingFragment<FragmentArchiveProjectBinding>(FragmentArchiveProjectBinding::inflate) {
    private lateinit var adapterProject: ProjectsAdapter

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }
}