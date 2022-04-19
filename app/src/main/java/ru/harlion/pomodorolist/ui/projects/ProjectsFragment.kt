package ru.harlion.pomodorolist.ui.projects

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentProjectsBinding
import ru.harlion.pomodorolist.ui.profile.premium.PremiumFragment
import ru.harlion.pomodorolist.ui.projects.lists_projects.archive.ArchiveProjectFragment
import ru.harlion.pomodorolist.ui.projects.adding.AddProjectFragment
import ru.harlion.pomodorolist.ui.projects.lists_projects.ListProjectsFragment
import ru.harlion.pomodorolist.utils.Prefs
import ru.harlion.pomodorolist.utils.replaceFragment


class ProjectsFragment : BindingFragment<FragmentProjectsBinding>(FragmentProjectsBinding::inflate) {

    private val viewModel : ProjectsVewModel by viewModels()
    private var countProjects = 0
    private lateinit var prefs: Prefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = Prefs(requireContext())

        binding.viewPager.adapter = ProjectViewPager(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.active)
                }
                else -> {
                    tab.text = getString(R.string.archive)
                }
            }
        }.attach()

        binding.addBtn.setOnClickListener {
            if (countProjects > 2 && !prefs.isPremium) {
                replaceFragment(PremiumFragment(), true)
            } else {
                replaceFragment(AddProjectFragment(), true)
            }

        }

        viewModel.projects.observe(viewLifecycleOwner, {
            countProjects = it.size
        })
    }

    class ProjectViewPager(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ListProjectsFragment()
                else -> ArchiveProjectFragment()
            }
        }
    }
}