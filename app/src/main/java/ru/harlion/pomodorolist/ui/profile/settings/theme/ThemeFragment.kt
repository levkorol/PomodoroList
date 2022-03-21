package ru.harlion.pomodorolist.ui.profile.settings.theme


import android.os.Bundle
import android.view.View
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentThemeBinding
import ru.harlion.pomodorolist.utils.Prefs


class ThemeFragment : BindingFragment<FragmentThemeBinding>(FragmentThemeBinding::inflate) {

    private lateinit var prefs : Prefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = Prefs(requireContext())

        binding.red.setOnClickListener {
            prefs.theme = "red"
            (activity as AppActivity).recreate()
        }

        binding.sky.setOnClickListener {
            prefs.theme = "sky"
            (activity as AppActivity).recreate()
        }

        binding.peach.setOnClickListener {
            prefs.theme = "peach"
            (activity as AppActivity).recreate()
        }

        binding.green.setOnClickListener {
            prefs.theme = "green"
            (activity as AppActivity).recreate()
        }

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as AppActivity).setBottomNavigationVisible(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppActivity).setBottomNavigationVisible(true)
    }
}