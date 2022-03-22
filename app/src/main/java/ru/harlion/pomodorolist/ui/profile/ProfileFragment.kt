package ru.harlion.pomodorolist.ui.profile


import android.os.Bundle
import android.view.View
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentProfileBinding
import ru.harlion.pomodorolist.ui.profile.settings.theme.ThemeFragment
import ru.harlion.pomodorolist.ui.profile.archive.ArchiveProjectFragment
import ru.harlion.pomodorolist.ui.profile.premium.PremiumFragment
import ru.harlion.pomodorolist.utils.replaceFragment


class ProfileFragment : BindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeTheme.setOnClickListener {
            replaceFragment(ThemeFragment(), true)
        }

        binding.archive.setOnClickListener {
            replaceFragment(ArchiveProjectFragment(), true)
        }

        binding.premium.setOnClickListener {
            replaceFragment(PremiumFragment(), true)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppActivity).updateNavigation(this)
    }
}