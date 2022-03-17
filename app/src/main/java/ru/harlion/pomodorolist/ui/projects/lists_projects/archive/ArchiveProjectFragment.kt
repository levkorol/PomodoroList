package ru.harlion.pomodorolist.ui.projects.lists_projects.archive


import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentArchiveProjectBinding


class ArchiveProjectFragment :
    BindingFragment<FragmentArchiveProjectBinding>(FragmentArchiveProjectBinding::inflate) {

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }
}