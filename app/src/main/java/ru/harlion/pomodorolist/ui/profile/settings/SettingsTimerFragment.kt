package ru.harlion.pomodorolist.ui.profile.settings


import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentSettingsTimerBinding


class SettingsTimerFragment : BindingFragment<FragmentSettingsTimerBinding>(FragmentSettingsTimerBinding::inflate) {


    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }

}