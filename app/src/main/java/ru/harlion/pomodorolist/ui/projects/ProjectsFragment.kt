package ru.harlion.pomodorolist.ui.projects

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import ru.harlion.pomodorolist.R
import ru.harlion.pomodorolist.base.BindingFragment
import ru.harlion.pomodorolist.databinding.FragmentProjectsBinding
import ru.harlion.pomodorolist.ui.projects.lists_projects.archive.ArchiveProjectFragment
import ru.harlion.pomodorolist.ui.projects.adding.AddProjectFragment
import ru.harlion.pomodorolist.ui.projects.lists_projects.ListProjectsFragment
import ru.harlion.pomodorolist.utils.replaceFragment


class ProjectsFragment : BindingFragment<FragmentProjectsBinding>(FragmentProjectsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            replaceFragment(AddProjectFragment(), true)
        }
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