package ru.harlion.pomodorolist.ui.profile


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import ru.harlion.pomodorolist.AppActivity
import ru.harlion.pomodorolist.R
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

        binding.mailDev.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:lev.dev.apps@gmail.com")
            }
            startActivity(Intent.createChooser(emailIntent, getString(R.string.mail)))
        }

        binding.googlePlay.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=ru.harlion.sushifox") //todo
            )
            startActivity(browserIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppActivity).updateNavigation(this)
    }
}